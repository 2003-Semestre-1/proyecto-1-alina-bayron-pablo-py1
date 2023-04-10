
package Model;

import java.util.ArrayList;

/**
 *
 * @author Bayron
 */
public class Memory {
    static ArrayList<String> colaEspera = new ArrayList<>();
    static ArrayList<BPC> listaBPCS = new ArrayList<>();
    static ArrayList<ArrayList<Object>> memoriaPrincipal = new ArrayList<>();
    static ArrayList<ArrayList<Object>> memoriaVirtual = new ArrayList<>();
    static ArrayList<ArrayList<Object>> UnidadAlmacenamiento = new ArrayList<>();
    static int pos = 0;
    
    public Memory(){
        for (int i = 0; i < 256; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            memoriaPrincipal.add(direction);
        }
        for (int i = 0; i < 64; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            memoriaVirtual.add(direction);
        }
        for (int i = 0; i < 512; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            UnidadAlmacenamiento.add(direction);
        }
    }
    
    public static void setMemoriaPrincipal(int pTamanio){
        memoriaPrincipal.clear();
        for (int i = 0; i < pTamanio; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            memoriaPrincipal.add(direction);
        }
    }
    public static void setmemoriaVirtual(int pTamanio){
        memoriaVirtual.clear();
        for (int i = 0; i < pTamanio; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            memoriaVirtual.add(direction);
        }
    }
    public static void setUnidadAlmacenamiento(int pTamanio){
        UnidadAlmacenamiento.clear();
        for (int i = 0; i < pTamanio; i++) {
            ArrayList<Object> direction = new ArrayList<>();
            UnidadAlmacenamiento.add(direction);
        }
    }
    
    public static void guardarBPC(BPC pBpc){
        listaBPCS.add(pBpc);
    }
    
    public static BPC getNetxBPC(){
        BPC bpc = listaBPCS.get(pos);
        pos++;
        return bpc;
    }
    
    public static boolean hayBcpPendientes(){
        if (pos >= listaBPCS.size()){
            return false;
        }else{
            return true;
        }
        
    }
    
    public static ArrayList getListaBPCS(){
        return listaBPCS;
    }
    
    public static void agregarAColaEspera(String proceso){
        colaEspera.add(proceso);
    }
    
    public static void eliminarProcesoColaEspera(String proceso){
        colaEspera.remove(proceso);
    }
    
    public static void limpiar(){
        colaEspera.clear();
        listaBPCS.clear();
        memoriaPrincipal.clear();
        memoriaVirtual.clear();
        UnidadAlmacenamiento.clear();
    }
}
