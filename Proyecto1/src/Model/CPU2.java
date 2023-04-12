
package Model;
import Controler.Controller;
import Model.BPC;
import static Model.CPU.pesoTotal;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author Bayron and Jose Pablo
 */
public class CPU2 {
    Controller controlador;
    static boolean cpuOcupado;
    static ArrayList<Object> instructions;
    static BPC bpc;
    static int totalInstrucciones;
    static int instruccionActual = 0;
    private static Date t1 = new Date();
    private static Date t2 = new Date();
    static int pesoTotal = 0;
    static String lineaActual;
    static long diff;
    static int entrada;
    // Registros AC, AX, BX, CX, DX
    private static int AC;
    private static int AX;
    private static int BX;
    private static int CX;
    private static int DX;
    
    public CPU2(){
        setCpuOcupado(false);
    }
    
    public static void cargarBCP(BPC pBpc){
        bpc= pBpc;
        bpc.setEstado("Preparado");
        setCpuOcupado(true);
        t1 = new Date();
        bpc.setTiempoInicio(t1);
        totalInstrucciones = bpc.getCodAsm().size() - 1;
        AC = 0;
        AX = 0;
        BX = 0;
        CX = 0;
        DX = 0;
        pesoTotal = 0;
        instruccionActual = 0;
    }
    
    public static void setCpuOcupado(boolean status){
        cpuOcupado = status;
    }
    
    public static boolean cpuOcupado(){
        return cpuOcupado;
    }
    public static void finalizar(){
        t2 = new Date();
        bpc.setTiempoFinal(t2);
        diff = t2.getTime() - t1.getTime() + 1;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        bpc.setTiempoTotal(seconds);
        bpc.setEstado("Finalizado");
        bpc.setTotalPeso(pesoTotal);
        Controller.agregarTimpos(bpc, 2);
        setCpuOcupado(false);
   }
    
    public static void executeLine() {
        bpc.setEstado("Ejecutando");
        String comand = bpc.getCodAsm().get(instruccionActual);
        comand = comand.replace(", ", " ");
        comand = comand.replace(",", " ");

        String[] comands = comand.split(" ");//mov ax 5

        String instruction = comands[0];
        Controller.actualizarTablaCPU(instruction, instruccionActual, 2);
        lineaActual = comand;
        String dir = "";
        String dir2 = "";
        String dir3 = "";
        int movValue = 0;

        switch (instruction) {
            case "LOAD":
                pesoTotal += 2;
                dir = comands[1];
                load(dir);
                break;
                case "STORE":
                pesoTotal += 2;
                dir = comands[1];
                store(dir);
                break;
            case "MOV":
                pesoTotal++;
                if (comands.length > 2) {
                    dir = comands[1];
                    if (comands[2].matches("[+-]?\\d*(\\.\\d+)?")) {
                        movValue = Integer.parseInt(comands[2]);
                        mov_valor_a_destino(movValue, dir);
                    } else {
                        dir2 = comands[2];
                        mov_destino_origen(dir, dir2);
                    }
                }
                break;
            case "ADD":
                pesoTotal += 3;
                dir = comands[1];
                add(dir);
                break;
            case "SUB":
                pesoTotal += 3;
                dir = comands[1];
                sub(dir);
                break;
            case "INC":
                pesoTotal += 1;
                if (comands.length > 1) {
                    dir = comands[1];
                    INC_destino(dir);
                } else {
                    INC();
                }
                break;
            case "DEC":
                pesoTotal += 1;
                if (comands.length > 1) {
                    dir = comands[1];
                    DEC_destino(dir);
                } else {
                    DEC();
                }
                break;
            case "SWAP":
                pesoTotal += 1;
                dir = comands[1];
                dir2 = comands[2];
                SWAP(dir, dir2);
                break;
            case "INT":
                pesoTotal += 1;
                dir = comands[1];
                if (dir.equals("20H")) {
                    INT_20H();
                }
                if (dir.equals("10H")) {
                    INT_10H();
                }
                if (dir.equals("09H")) {
                    INT_09H();
                }
                //falta el 21h
                break;
            case "JMP":
                pesoTotal += 2;
                dir = comands[1];
                JMP(Integer.parseInt(dir));
                break;
            case "CMP":
                pesoTotal += 2;
                dir = comands[1];
                dir2 = comands[2];
                bpc.setCMP(CMP(dir, dir2));
                break;
            case "JE":
                pesoTotal += 2;
                dir = comands[1];
                JE(Integer.parseInt(dir));
                break;
            case "JNE":
                pesoTotal += 2;
                dir = comands[1];
                JNE(Integer.parseInt(dir));
                break;
            case "PARAM":
                pesoTotal += 3;
                if (comands.length == 2) {
                    dir = comands[1];
                    PARAM1(Integer.parseInt(dir));
                }
                if (comands.length == 3) {
                    dir = comands[1];
                    dir2 = comands[2];
                    PARAM2(Integer.parseInt(dir), Integer.parseInt(dir2));
                }
                if (comands.length == 4) {
                    dir = comands[1];
                    dir2 = comands[2];
                    dir3 = comands[3];
                    PARAM3(Integer.parseInt(dir), Integer.parseInt(dir2), Integer.parseInt(dir3));
                }
                break;
            case "PUSH":
                pesoTotal += 1;
                dir = comands[1];
                PUSH(dir);
                break;
            case "POP":
                pesoTotal += 1;
                dir = comands[1];
                POP(dir);
                break;
        }
        if (totalInstrucciones <= instruccionActual){
            finalizar();
        }
        bpc.setTotalPeso(pesoTotal);
        Controller.actualizarRegistrosCPU(bpc, 2);
        instruccionActual ++;
    }
public static void mov_valor_a_destino(int valor, String destino) {
        switch (destino) {
            case "AX":
                bpc.setAX(valor);
                break;
            case "BX":
                bpc.setBX(valor);
                break;
            case "CX":
                bpc.setCX(valor);
                break;
            case "DX":
                bpc.setDX(valor);
                break;
        }
    }

    public static void mov_destino_origen(String destino, String origen) {
        switch (destino) {
            case "AX":
                bpc.setAX(get_valor_registro(origen));
                break;
            case "BX":
                bpc.setBX(get_valor_registro(origen));
                break;
            case "CX":
                bpc.setCX(get_valor_registro(origen));
                break;
            case "DX":
                bpc.setDX(get_valor_registro(origen));
                break;
        }
    }

    public static void load(String x) {
        switch (x) {
            case "AX":
                bpc.setAC(bpc.getAX());
                break;
            case "BX":
                bpc.setAC(bpc.getBX());
                break;
            case "CX":
                bpc.setAC(bpc.getCX());
                break;
            case "DX":
                bpc.setAC(bpc.getDX());
                break;
        }
    }

    public static void add(String x) {
        switch (x) {
            case "AX":
                bpc.setAC(bpc.getAC() + bpc.getAX());
                break;
            case "BX":
                bpc.setAC(bpc.getAC() + bpc.getBX());
                break;
            case "CX":
                bpc.setAC(bpc.getAC() + bpc.getCX());
                break;
            case "DX":
                bpc.setAC(bpc.getAC() + bpc.getDX());
                break;
        }
    }

    public static void sub(String x) {
        switch (x) {
            case "AX":
                bpc.setAC(bpc.getAC() - bpc.getAX());
                break;
            case "BX":
                bpc.setAC(bpc.getAC() - bpc.getBX());
                break;
            case "CX":
                bpc.setAC(bpc.getAC() - bpc.getCX());
                break;
            case "DX":
                bpc.setAC(bpc.getAC() - bpc.getDX());
                break;
        }
    }

    public static void store(String x) {
        switch (x) {
            case "AX":
                bpc.setAX(bpc.getAC());
                break;
            case "BX":
                bpc.setBX(bpc.getAC());
                break;
            case "CX":
                bpc.setCX(bpc.getAC());
                break;
            case "DX":
                bpc.setDX(bpc.getAC());
                break;
        }
    }

    public static void INC() {
        bpc.setAC(bpc.getAC() + 1);
    }

    public static void INC_destino(String x) {
        switch (x) {
            case "AX":
                bpc.setAX(bpc.getAX() + 1);
                break;
            case "BX":
                bpc.setBX(bpc.getBX() + 1);
                break;
            case "CX":
                bpc.setCX(bpc.getCX() + 1);
                break;
            case "DX":
                bpc.setDX(bpc.getDX() + 1);
                break;
        }
    }

    public static void DEC() {
        bpc.setAC(bpc.getAC() - 1);
    }

    public static void DEC_destino(String x) {
        switch (x) {
            case "AX":
                bpc.setAX(bpc.getAX() - 1);
                break;
            case "BX":
                bpc.setBX(bpc.getBX() - 1);
                break;
            case "CX":
                bpc.setCX(bpc.getCX() - 1);
                break;
            case "DX":
                bpc.setDX(bpc.getDX() + 1);
                break;
        }
    }

    public static void SWAP(String registro1, String registro2) {
        int AUX = 0;
        switch (registro1) {
            case "AX":
                AUX = get_valor_registro(registro2);
                mov_destino_origen(registro2, "AX");
                bpc.setAX(AUX);
                break;
            case "BX":
                AUX = get_valor_registro(registro2);
                mov_destino_origen(registro2, "BX");
                bpc.setBX(AUX);
                break;
            case "CX":
                AUX = get_valor_registro(registro2);
                mov_destino_origen(registro2, "CX");
                bpc.setCX(AUX);
                break;
            case "DX":
                AUX = get_valor_registro(registro2);
                mov_destino_origen(registro2, "DX");
                bpc.setDX(AUX);
                break;
        }
    }

    public static void INT_20H() {
        bpc.setIndexAux(bpc.getCodAsm().size());
    }
    
    public static void INT_10H() {
        Controller.imprimirEnPantalla(bpc.getDX(), bpc.getCPU());
        
    }

    public static void INT_09H() {
        System.out.println("en cpu 2 envia: " + bpc.getCPU());
        Controller.solicitarEntradaTexto(bpc.getCPU());
    }
    
    public static void recibirTextoINTH09(int num){
        bpc.setDX(num);
        Controller.actualizarRegistrosCPU(bpc, 2);
    }

    public static boolean CMP(String registro1, String registro2) {
        boolean bandera = false;

        if (get_valor_registro(registro1) == get_valor_registro(registro2)) {
            bandera = true;
        } else {
            bandera = false;
        }

        return bandera;
    }

    public static void JMP(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0) {
            bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento");
        }

    }

    public static void JE(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0 && bpc.isCMP()) {
            bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento o CMP falso");
            System.out.println("error de desbordamiento o CMP false");
        }
    }

    public static void JNE(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0 && bpc.isCMP()) {
            bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento o CMP falso");
            System.out.println("error de desbordamiento o CMP false");
        }
    }

    public static void PARAM1(int v1) {
        bpc.getPila().push(v1);
    }

    public static void PARAM2(int v1, int v2) {
        bpc.getPila().push(v1);
        bpc.getPila().push(v2);
    }

    public static void PARAM3(int v1, int v2, int v3) {
        bpc.getPila().push(v1);
        bpc.getPila().push(v2);
        bpc.getPila().push(v3);
    }

    public static void PUSH(String registro) {
        switch (registro) {
            case "AX":
                bpc.getPila().push(bpc.getAX());
                break;
            case "BX":
                bpc.getPila().push(bpc.getBX());
                break;
            case "CX":
                bpc.getPila().push(bpc.getCX());
                break;
            case "DX":
                bpc.getPila().push(bpc.getDX());
                break;
        }
    }

    public static void POP(String registro) {
        switch (registro) {
            case "AX":
                bpc.setAX(Integer.parseInt(bpc.getPila().pop().toString()));
                break;
            case "BX":
                bpc.setBX(Integer.parseInt(bpc.getPila().pop().toString()));
                break;
            case "CX":
                bpc.setCX(Integer.parseInt(bpc.getPila().pop().toString()));
                break;
            case "DX":
                bpc.setDX(Integer.parseInt(bpc.getPila().pop().toString()));
                break;
        }
    }

    private static int get_valor_registro(String registro) {
        int flag = -1;
        switch (registro) {
            case "AX":
                flag = bpc.getAX();
                break;
            case "BX":
                flag = bpc.getBX();
                break;
            case "CX":
                flag = bpc.getCX();
                break;
            case "DX":
                flag = bpc.getDX();
                break;
        }
        return flag;
    }

    public static void clean() {
        AC = 0;
        AX = 0;
        BX = 0;
        CX = 0;
        DX = 0;
    } 
}
