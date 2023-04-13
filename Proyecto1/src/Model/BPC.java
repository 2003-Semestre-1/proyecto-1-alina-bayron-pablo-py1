
package Model;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
/**
 *
 * @author Bayron and Jose Pablo
 */
public class BPC {
    //Nombre
    private String nombre;
    // Estados; nuevo, preparado, ejecución, en espera, finalizado
    private String estado;
    // Registros AC, AX, BX, CX, DX
    private int AC;
    private int AX;
    private int BX;
    private int CX;
    private int DX;
    // Contador del programa (ubicación del programa cargado en memoria)
    private long direccionMemoria;
    //Información de la pila: definir tamaño de 10, y tomar en cuenta error de desbordamiento
    Stack pila = new Stack();
    // Información contable; el cpu donde se está ejecutando, tiempo de inicio, tiempo empleado.
    private int CPU;
    private Date horaInicio;
    private Date horaFinal;
    private long tiempoTotal;
    // Información del estado de E/S; lista de archivos abiertos
    private ArrayList<Object> listArchivos = new ArrayList<>();
    //Bloque de memoria compartida
    private int memoriaCompartida;
    // Enlace al siguiente BPC
    private int siguienteBPC;
    // Dirección de inicio (BASE)
    private int direccionInicio;
    // Tamaño proceso
    private int TamanioProceso;
    // Prioridad
    private int Prioridad;
    /*
    • Dirección inicio (Base)
    • Tamaño del proceso (Alcance)
    */
    //Prioridad (pos de llegada)
    private int prioridad;
    // Linea actual
    private String lineaActual;
    // CMP
    private boolean CMP;
    //indexAux
    private int indexAux;
    // lias de codigo
    private ArrayList<String> codLineasAsm = new ArrayList<>();
    //linea actual
    private int linea = 0;
    
    public BPC(String nombre, ArrayList<String> linesAsm, int posicionMemoria) {
        codLineasAsm.clear();
        codLineasAsm = linesAsm;
        this.nombre = nombre;
        direccionMemoria = posicionMemoria;
        siguienteBPC = posicionMemoria+1;
        Prioridad = posicionMemoria;
        setIndexAux(0);
        setAC(0);
        setAX(0);
        setBX(0);
        setCX(0);
        setDX(0);
        setTotalPeso(0);
        setEstado("Nuevo");
        setCMP(false);
    }


    //--------------------------------------------- Gets & sets ---------------------------------------------//
    public String getNombre(){
        return nombre;
    }
    
    public ArrayList<String> getCodAsm() {
        return codLineasAsm;
    }

    public void setCodAsm(ArrayList<String> codAsm) {
        this.codLineasAsm = codAsm;
    }

    public int getAC() {
        return AC;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public int getAX() {
        return AX;
    }

    public void setAX(int AX) {
        this.AX = AX;
    }

    public int getBX() {
        return BX;
    }

    public void setBX(int BX) {
        this.BX = BX;
    }

    public int getCX() {
        return CX;
    }

    public void setCX(int CX) {
        this.CX = CX;
    }

    public int getDX() {
        return DX;
    }

    public void setDX(int DX) {
        this.DX = DX;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getDireccionMemoria() {
        return direccionMemoria;
    }

    public void setDireccionMemoria(int direccionMemoria) {
        this.direccionMemoria = direccionMemoria;
    }

    public Stack getPila() {
        return pila;
    }

    public void setPila(Stack pila) {
        this.pila = pila;
    }

    public int getCPU() {
        return CPU;
    }

    public void setCPU(int CPU) {
        this.CPU = CPU;
    }

    public Date getTiempoInicio() {
        return horaInicio;
    }

    public void setTiempoInicio(Date tiempoInicio) {
        this.horaInicio = tiempoInicio;
    }
    
    public Date getTiempoFinal() {
        return horaFinal;
    }

    public void setTiempoFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public String getLineaActual() {
        return lineaActual;
    }

    public void setLineaActual(String lineaActual) {
        this.lineaActual = lineaActual;
    }

    public boolean isCMP() {
        return CMP;
    }

    public void setCMP(boolean CMP) {
        this.CMP = CMP;
    }

    public int getIndexAux() {
        return indexAux;
    }

    public void setIndexAux(int indexAux) {
        this.indexAux = indexAux;
    }

    public void incrementAux() {
        this.indexAux++;
    }

    public int getTotalPeso() {
        return TamanioProceso;
    }
    
    public void setTotalPeso(int pTamanio){
        TamanioProceso = pTamanio;
    }
    
    public int getDireccionMemoria2 (){
        return direccionInicio;
    }
    
}


