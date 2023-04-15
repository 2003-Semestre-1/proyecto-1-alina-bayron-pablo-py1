
package Model;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Bayron
 */
public class Memory {
    static float tamanioUnidad = 512;
    static float tamanioPrincipal = 256;
    static float tamanioVirtual = 64;
    static float tamanioUnidadReserva = 512;
    static float tamanioPrincipalReserva = 256;
    static float tamanioVirtualReserva = 64;
    static ArrayList<BPC> colaEspera = new ArrayList<>();
    static ArrayList<BPC> memoriaPrincipal = new ArrayList<>();
    static ArrayList<BPC> memoriaVirtual = new ArrayList<>();
    static ArrayList<ArrayList> UnidadAlmacenamiento = new ArrayList<>();
    static int pos = 0;
    static int posVirtual = 0;
    
    // debe existir un indice para los archivos nombre y indice y direccion donde se almacena
    // bpc = 27 campos + lista de archivos
    
    public static void setMemoriaPrincipal(int pTamanio){
        tamanioPrincipal = pTamanio;
        
    }
    public static void setmemoriaVirtual(int pTamanio){
        tamanioVirtual = pTamanio;
    }
    public static void setUnidadAlmacenamiento(int pTamanio){
        tamanioUnidad = pTamanio;
    }
    
    public static void guardarBPC(BPC pBpc){
        int tamanioCodASM = pBpc.getCodAsm().size();
        double tamanioSuponisionPrincipal = 0.808 + (tamanioCodASM / 1024.0);
        double tamanioSuponisionVirtual = 0.808 + (tamanioCodASM / 1024.0);
        if ((tamanioPrincipal - tamanioSuponisionPrincipal) >= 0 || (tamanioVirtual - tamanioSuponisionVirtual) >= 0){
            ArrayList<Object> datosBpc = new ArrayList();
            datosBpc.add(pBpc.getDireccionMemoria());
            datosBpc.add(pBpc.getNombre());
            datosBpc.add(pBpc.getDireccionMemoria());
            datosBpc.add(pBpc.getCodAsm());
            if ((tamanioPrincipal - (0.808 + (tamanioCodASM / 1024.0) )) > 0 ){
                UnidadAlmacenamiento.add(datosBpc);
                memoriaPrincipal.add(pBpc);
                disminuirTamanioUnidad(tamanioCodASM, 1);
            }else{
                UnidadAlmacenamiento.add(datosBpc);
                memoriaVirtual.add(pBpc);
                disminuirTamanioUnidad(tamanioCodASM, 2);
            }
            colaEspera.add(pBpc);
        } else {
            JOptionPane.showMessageDialog(null, "No hay suficiente memoria principal y virtual para cargar el BCP", "Error", JOptionPane.ERROR_MESSAGE);
        }         
    }
    
    public static BPC getNetxBPC(){
        System.out.println("Tamanio de la lista" + memoriaPrincipal.size());
        for(int i = 0; i<memoriaPrincipal.size(); i++){
            BPC prueba = memoriaPrincipal.get(i);
            System.out.println("Nombre:" + prueba.getNombre());
        }
        System.out.println("Nombre:" );
        if (pos < memoriaPrincipal.size()){
            System.out.println("Entro al if:" );
            BPC bpc = memoriaPrincipal.get(pos);
            System.out.println("pos: " + pos );
            pos++;
            System.out.println("Nombre:" + bpc.getNombre());
            return bpc;
        }else if (posVirtual < memoriaVirtual.size()){
            BPC bpc = memoriaVirtual.get(posVirtual);
            posVirtual++;
            return bpc;
        }else {
            return null;
        }
        
    }
    
    public static boolean hayBcpPendientes(){
        return pos <= memoriaPrincipal.size() || posVirtual <= memoriaVirtual.size();
        
    }
    
    public static ArrayList getListaBPCS(){
        return colaEspera;
        
    }
        
    public static void disminuirTamanioUnidad(int tamanioCodASM, int memoria){
        
        if (memoria == 1){
            tamanioPrincipal -= 0.808 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }else{
            tamanioVirtual -= 0.808 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }
        
        tamanioUnidad -= 0.100 + (tamanioCodASM / 1024.0);
    }
    
    public static void aumentarTamanioUnidad(int tamanioCodASM, int memoria){
        if (memoria == 1){
            tamanioPrincipal += 0.808 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }else{
            tamanioVirtual += 0.808 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }        
    }
    
    public static void limpiar(){
        colaEspera.clear();
        memoriaPrincipal.clear();
        UnidadAlmacenamiento.clear();
        memoriaVirtual.clear();
        UnidadAlmacenamiento.clear();
        pos = 0;
        posVirtual = 0;
        tamanioPrincipal = tamanioPrincipalReserva;
        tamanioVirtual = tamanioVirtualReserva;
        tamanioUnidad = tamanioUnidadReserva;
    }
    
    public static void eliminarBPC (BPC objeto){
        System.out.println("A eliminar llega " + objeto.getNombre());
        aumentarTamanioUnidad(objeto.getCodAsm().size(),1);
        System.out.println("Con la posiscion de memoria: " + objeto.getDireccionMemoria2());
        memoriaPrincipal.remove(objeto.getDireccionMemoria2());
        pos--;
        if (!memoriaVirtual.isEmpty()){
            BPC bpc = memoriaVirtual.get(0);
            memoriaPrincipal.add(bpc);
            memoriaVirtual.remove(0);
            aumentarTamanioUnidad(bpc.getCodAsm().size(),2);
            disminuirTamanioUnidad(bpc.getCodAsm().size(),1);
        }
        System.out.println("Tamanio de la lista" + memoriaPrincipal.size());
        for(int i = 0; i<memoriaPrincipal.size(); i++){
            BPC prueba = memoriaPrincipal.get(i);
            System.out.println("Nombre:" + prueba.getNombre());
        }
    }
}