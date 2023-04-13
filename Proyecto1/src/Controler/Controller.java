package Controler;

import Visual.Interface;
import Model.BPC;
import Model.Memory;
import Model.CPU;
import Model.CPU2;
import Visual.InterfaceConfiguracion;
import Visual.InterfaceMensajes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;

/**
 *
 * @author Bayron
 */
public class Controller {
    
    static int procesoNumeroCPU1 = 0;
    static int procesoNumeroCPU2 = 0;
    static int timeCPU1 = 0;
    static int timeCPU2 = 0;
    static BPC bpcActual;
    static boolean avisoCPU1 = false;
    static boolean avisoCPU2 = false;
    static boolean hayProcesos = true;
    static boolean modoAutomatico = false;
    
    static boolean interrumpir = false;
    
   
    
    public Controller(Interface interfaz, Reader reader, Memory memoria, CPU cpu1, CPU cpu2){
    }
    
    
    public static void btnCargarArchivos(JFileChooser chooser){
        Reader.fileUpload(chooser);
        cargarBpcs();
        Interface.fillProcessTable(Memory.getListaBPCS());
    }
    
    public static void actualizarTablaCPU(String instruccion, int pos, int cpu){
        if (cpu == 1){
            Interface.actualizarTablaCPU(instruccion, pos, cpu);
        }else{
            Interface.actualizarTablaCPU(instruccion, pos, cpu);
        }
    }
    
    public static void cargarBpcs(){
        hayProcesos=true;
        bpcActual = getNextBPC();
        System.out.println(bpcActual == null);
        if (!CPU.cpuOcupado()){
            if (bpcActual != null){
                CPU.cargarBCP(bpcActual);
                Interface.limpiarDatosInterfaceCpu(1);
                bpcActual.setCPU(1);
                bpcActual.setEstado("Preparado");
                marcarTiempoCPU(bpcActual.getNombre(), procesoNumeroCPU1, 0, 1 );
                bpcActual = getNextBPC();
            }
        }
        if (!CPU2.cpuOcupado()){
            if (bpcActual != null){
                CPU2.cargarBCP(bpcActual);
                Interface.limpiarDatosInterfaceCpu(2);
                bpcActual.setCPU(2);
                bpcActual.setEstado("Preparado");
                marcarTiempoCPU(bpcActual.getNombre(), procesoNumeroCPU2, 0, 2 );
                bpcActual = getNextBPC();
            }
        }
        Interface.esconderBtnConfiguracion();
    }

    public static void ejecutarSiguienteInstruccion(){
        System.out.println("CP1: " + CPU.cpuOcupado());
        System.out.println("CP2: " + CPU2.cpuOcupado());
        System.out.println(bpcActual == null);

        if (bpcActual == null && !CPU.cpuOcupado() && !CPU2.cpuOcupado() ){
            System.out.println("Entro al 1 if");
            hayProcesos = false;
            Interface.noHayProcesos(3);
            
        }else{
            System.out.println("Entro al 1 else");
            if (!CPU.cpuOcupado()){
                if (bpcActual != null){
                    procesoNumeroCPU1 ++;
                    Interface.limpiarDatosInterfaceCpu(1);
                    bpcActual.setCPU(1);
                    CPU.cargarBCP(bpcActual);
                    CPU.executeLine();
                    timeCPU1++;
                    marcarTiempoCPU(bpcActual.getNombre(), procesoNumeroCPU1, 0, 1);
                    marcarTiempoCPU("X", procesoNumeroCPU1, timeCPU1, 1);
                    bpcActual = getNextBPC();
                }else{
                    if (!avisoCPU1 && !modoAutomatico){
                        avisoCPU1 = true;
                        Interface.noHayProcesos(1);
                    }
                    
                }
            }else{
                timeCPU1++;
                marcarTiempoCPU("X", procesoNumeroCPU1, timeCPU1, 1);
                CPU.executeLine();
            }

            if (!CPU2.cpuOcupado()){
                if (bpcActual != null){
                    procesoNumeroCPU2 ++;
                    Interface.limpiarDatosInterfaceCpu(2);
                    bpcActual.setCPU(2);
                    CPU2.cargarBCP(bpcActual);
                    CPU2.executeLine();
                    timeCPU2++;
                    marcarTiempoCPU(bpcActual.getNombre(), procesoNumeroCPU2, 0, 2);
                    marcarTiempoCPU("X", procesoNumeroCPU2, timeCPU2, 2);
                    bpcActual = getNextBPC();
                    
                }else{
                    if (!avisoCPU2 && !modoAutomatico){
                        avisoCPU2 = true;
                        Interface.noHayProcesos(2);
                    }
                }
            }else{
                timeCPU2++;
                marcarTiempoCPU("X", procesoNumeroCPU2, timeCPU2, 2 );
                CPU2.executeLine();
            }
        }
        
        Interface.fillProcessTable(Memory.getListaBPCS());
    }
    
    public static void resolverAutomatico() throws InterruptedException{
        modoAutomatico = true;
        ScheduledExecutorService automatico = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            ejecutarSiguienteInstruccion();
            if (!hayProcesos){
                automatico.shutdown();
            } else if (interrumpir){
                automatico.shutdown();
                interrumpir = false;
            }
        };

        int initialDelay = 0; // Demora inicial antes de ejecutar la tarea por primera vez
        int period = 1; // Intervalo entre ejecuciones consecutivas en segundos

        automatico.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    }
    public static void actualizarRegistrosCPU(BPC bpc, int cpu){
        if (cpu == 1){
            Interface.actualizarRegistrosCPU(bpc, cpu);
        }else{
            Interface.actualizarRegistrosCPU(bpc, 2);
        }
    }
  
    public static void newBPC(String nombre, ArrayList<String> linesAsm, int posicionMemoria){
        BPC bpc = new BPC(nombre,linesAsm,posicionMemoria);
        Memory.guardarBPC(bpc);
    }
    
    public static BPC getNextBPC(){
        if (Memory.hayBcpPendientes()){
            System.out.println("entro al if de getNextBpc");
            BPC bpc = Memory.getNetxBPC();
            return bpc;
        }else{
            System.out.println("entro al else de getNextBpc");
            return null;
        }
    }

    public static void imprimirEnPantalla(int DX, int cpu){
        Interface.imprimerEnPantalla(DX, cpu);
    }
    
    public static void solicitarEntradaTexto(int CPU){
        System.out.println("el cpu en controler es: " + CPU);
        Interface.entradaTexto(CPU, false, interrumpir);
        interrumpir = true;
        
    }
    
    public static void enviarTexto(int num, int cpu){
        if (cpu == 1){
            CPU.recibirTextoINTH09(num);
        }else{
            CPU2.recibirTextoINTH09(num);
        }
        
    }
    
    public static void marcarTiempoCPU(String valor, int row, int colum, int cpu){
        if (cpu == 1){
            Interface.marcarTiempoCPU(row, colum, valor, cpu);
        }else{
            Interface.marcarTiempoCPU(row, colum, valor, cpu);
        }
    }
    public static void agregarTimpos (BPC bpc, int cpu){
        Interface.agregarTiempos(bpc, cpu);
    }
    
    public static void mostrarEstadisticas (){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        ArrayList<BPC> bpcs = Memory.getListaBPCS();
        for(int i = 0; i<bpcs.size(); i++){
            System.out.println(i);
        }
        String mensaje = "";
        if (bpcs.isEmpty()){
            mensaje = "No hay BCPS para mostrar";
        }else{
            for(int i = 0; i<bpcs.size(); i++){
                BPC bpc = bpcs.get(i);
                String inicio = "";
                String fin = "Aún no termina de ejecutar";
                String total = "Aún no termina de ejecutar";
                if (!"Nuevo".equals(bpc.getEstado()) && !"Preparado".equals(bpc.getEstado()) && bpc.getEstado() != null ){
                    if (bpc.getTiempoInicio() != null){
                        d1 = bpc.getTiempoInicio();
                        inicio = format.format(d1);
                    }
                    if (bpc.getTiempoFinal()!= null){
                        d2 = bpc.getTiempoFinal();
                        fin = format.format(d2);
                        total = String.valueOf(bpc.getTiempoTotal() + " segundos");
                    }                   
                    mensaje += 
                            " Archivo: " + bpc.getNombre() +
                            "\n        ➤ Direccion de memoria: " + bpc.getDireccionMemoria()+
                            "\n        ➤ Estado: " + bpc.getEstado() +
                            "\n        ➤ Ejecutado por el cpu: " + bpc.getCPU()+
                            "\n        ➤ Registros: " +
                            "\n                ➢ AC: " + bpc.getAC()+ 
                            "\n                ➢ AX: " + bpc.getAX()+ 
                            "\n                ➢ BX: " + bpc.getBX() + 
                            "\n                ➢ CX: " + bpc.getCX() +
                            "\n                ➢ DX: " + bpc.getDX() +
                            "\n        ➤ Hora de inicio: " + inicio + 
                            "\n        ➤ Hora final: " + fin + 
                            "\n        ➤ Tiempo total: " + total + 
                            "\n        ➤ Peso total de las instrucciones: " + bpc.getTotalPeso() +
                            "\n\n - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n\n"; 
                            
                            
                }else{
                    mensaje += "Archivo: " + bpc.getNombre() +
                        "\n \t ➤ Direccion de memoria: " + bpc.getDireccionMemoria()+
                        "\n \t ➤ Estado: " + bpc.getEstado() + "\n" + 
                        "\n\n - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n";
                }
            }
        
        }
        mensaje+= "\n - - - - - - - - - - - Fin de las estadísticas - - - - - - - - - - - -\n";
        InterfaceMensajes ventanaEstadisticas = new InterfaceMensajes(mensaje);
        ventanaEstadisticas.setVisible(true);
    }
    
    public static void mostrarVentanaConfiguracion(){
        InterfaceConfiguracion ventanaConfiguracion = new InterfaceConfiguracion();
        ventanaConfiguracion.setVisible(true);
    }
    public static void guardarTamaniosMemoria(int principal, int virtual, int unidad){
        boolean bandera = false;
        if (principal != 256){
            Memory.setMemoriaPrincipal(principal);
            bandera = true;
        }
        if (virtual != 64){
            Memory.setmemoriaVirtual(virtual);
            bandera = true;
        }
        if (unidad != 512){
            Memory.setUnidadAlmacenamiento(unidad);
            bandera = true;
        }
        if (bandera){
            InterfaceConfiguracion.informar();
        }
    }
    
    public static void limpiar(){
        procesoNumeroCPU1 = 0;
        procesoNumeroCPU2 = 0;
        timeCPU1 = 0;
        timeCPU2 = 0;
        Memory.limpiar();
        Interface.eliminarDatosTablaTiempos();
        Interface.limpiarTablaProcesos();
        CPU.finalizar();
        CPU2.finalizar();
        Interface.limpiarDatosInterfaceCpu(2);
        Interface.limpiarDatosInterfaceCpu(1);
    }
}
