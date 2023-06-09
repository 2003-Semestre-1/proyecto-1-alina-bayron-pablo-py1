
package Model;
import Controler.Controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author Bayron and Jose Pablo
 */
public class CPU {
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
    static String nombreArchivo = "archivoCPU1.txt";
    static String textoAEscribir = "Este es el texto a escribir en el archivo del cpu1.";
    static String textoLeido = "";
    
    public CPU(){
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
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String TFinal;
        t2 = new Date();
        TFinal = format.format(2);
        bpc.setTiempoFinal(t2);
        diff = t2.getTime() - t1.getTime() + 1;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        bpc.setTiempoTotal(seconds);
        bpc.setEstado("Finalizado");
        bpc.setTotalPeso(pesoTotal);
        Controller.agregarTimpos(bpc, 1);
        setCpuOcupado(false);
        Memory.eliminarBPC(bpc);
    }
    
    public static void executeLine() {
        bpc.setEstado("Ejecutando");
        String comand = bpc.getCodAsm().get(instruccionActual);
        comand = comand.replace(", ", " ");
        comand = comand.replace(",", " ");

        String[] comands = comand.split(" ");//mov ax 5

        String instruction = comands[0];
        Controller.actualizarTablaCPU(instruction, instruccionActual, 1);
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
                    }else if ("AH".equals(dir)){
                        bpc.setAH(comands[2]);
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
                if (dir.equals("21H")) {
                    INT_21H(bpc.getAH());
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
        Controller.actualizarRegistrosCPU(bpc, 1);
        instruccionActual ++;
        bpc.setIndexAux(instruccionActual);
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
        finalizar();
    }
    
    public static void INT_10H() {
        Controller.imprimirEnPantalla(bpc.getDX(), bpc.getCPU());
        
    }

    public static void INT_09H() {
        Controller.solicitarEntradaTexto(bpc.getCPU());
    }
    
    public static void INT_21H(String comando) {
        if ("3ch".equals(comando)){ // crear el archivo
            try {
            FileOutputStream archivoNuevo = new FileOutputStream(nombreArchivo);
            archivoNuevo.close();
            JOptionPane.showMessageDialog(null, "Archivo creado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al crear archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if ("3dh".equals(comando)){ // abrir el archivo
            try {
                FileInputStream archivoExistente = new FileInputStream(nombreArchivo);
                JOptionPane.showMessageDialog(null, "Archivo abierto exitosamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                int c;
                while ((c = archivoExistente.read()) != -1) {
                    textoLeido += (char) c;
                }
                archivoExistente.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al abrir archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if ("4dh".equals(comando)){ // leer el archivo
            try {
                FileInputStream archivoExistente = new FileInputStream(nombreArchivo);
                JOptionPane.showMessageDialog(null, "Leyendo archivo...", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                int c;
                while ((c = archivoExistente.read()) != -1) {
                    textoLeido += (char) c;
                }
                archivoExistente.close();
                JOptionPane.showMessageDialog(null, "Texto leído: " + textoLeido, "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al leer archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if ("40h".equals(comando)){ //escribir en el archivo
            try {
                FileOutputStream archivoExistente = new FileOutputStream(nombreArchivo);
                byte[] bytesAEscribir = textoAEscribir.getBytes();
                archivoExistente.write(bytesAEscribir);
                archivoExistente.close();
                JOptionPane.showMessageDialog(null, "Texto escrito exitosamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al escribir archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if ("41h".equals(comando)){ // eliminar archivo
            try {
                File archivoAEliminar = new File(nombreArchivo);
                if (archivoAEliminar.delete()) {
                    JOptionPane.showMessageDialog(null, "Archivo eliminado exitosamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar archivo.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public static void recibirTextoINTH09(int num){
        bpc.setDX(num);
        Controller.actualizarRegistrosCPU(bpc, 1);
    }

    public static boolean CMP(String registro1, String registro2) {
        boolean bandera = false;
        
        if (get_valor_registro(registro1) == get_valor_registro(registro2)) {
            bandera = true;
            bpc.setCMP(true);
            
        } else {
            bandera = false;
            bpc.setCMP(false);
        }

        return bandera;
    }

    public static void JMP(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0) {
            bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
            instruccionActual += desplazamiento - 1;
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento");
        }

    }

    public static void JE(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0) {
            if (bpc.isCMP()){
                bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
                instruccionActual += desplazamiento - 1;
            }else{
                JOptionPane.showMessageDialog(null, "CMP falso, por lo que no salta");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento");
        }
    }

    public static void JNE(int desplazamiento) {
        if ((bpc.getIndexAux() + desplazamiento) < bpc.getCodAsm().size() && (bpc.getIndexAux() + desplazamiento) >= 0 && bpc.isCMP()) {
            if (!bpc.isCMP()){
                bpc.setIndexAux(bpc.getIndexAux() + desplazamiento);//5 + -1 = 4
                instruccionActual += desplazamiento - 1;
            }else{
                JOptionPane.showMessageDialog(null, "CMP verdadero, por lo que no salta");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Error de desbordamiento");
        }
    }

    public static void PARAM1(int v1) {
        if (bpc.getPila().size() <10){
            bpc.getPila().push(v1);
        }    
    }

    public static void PARAM2(int v1, int v2) {
        if (bpc.getPila().size() <10){
            bpc.getPila().push(v1);
        }
        if (bpc.getPila().size() <10){        
            bpc.getPila().push(v2);
        }
    }

    public static void PARAM3(int v1, int v2, int v3) {
        if (bpc.getPila().size() <10){
            bpc.getPila().push(v1);
        }
        if (bpc.getPila().size() <10){        
            bpc.getPila().push(v2);
        }
        if (bpc.getPila().size() <10){       
            bpc.getPila().push(v3);
        }
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
