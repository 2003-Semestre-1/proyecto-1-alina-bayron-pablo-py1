
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
    static ArrayList<String> colaEspera = new ArrayList<>();
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
        double tamanioSuponisionPrincipal = 0.488 + (tamanioCodASM / 1024.0);
        double tamanioSuponisionVirtual = 0.488 + (tamanioCodASM / 1024.0);
        if ((tamanioPrincipal - tamanioSuponisionPrincipal) >= 0 || (tamanioVirtual - tamanioSuponisionVirtual) >= 0){
            ArrayList<Object> datosBpc = new ArrayList();
            datosBpc.add(pBpc.getDireccionMemoria());
            datosBpc.add(pBpc.getNombre());
            datosBpc.add(pBpc.getDireccionMemoria());
            datosBpc.add(pBpc.getCodAsm());
            if ((tamanioPrincipal - (0.488 + (tamanioCodASM / 1024.0) )) > 0 ){
                System.out.println(pBpc.getNombre()+"se guardo en principal");
                UnidadAlmacenamiento.add(datosBpc);
                memoriaPrincipal.add(pBpc);
                disminuirTamanioUnidad(tamanioCodASM, 1);
            }else{
                UnidadAlmacenamiento.add(datosBpc);
                System.out.println(pBpc.getNombre()+"se guardo en virtual");
                memoriaVirtual.add(pBpc);
                disminuirTamanioUnidad(tamanioCodASM, 2);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay suficiente memoria principal y virtual para cargar el BCP", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
            
           
    }
    
    public static BPC getNetxBPC(){
        if (pos < memoriaPrincipal.size()){
            BPC bpc = memoriaPrincipal.get(pos);
            pos++;
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
        ArrayList<BPC> listBpc = new ArrayList();
        System.out.println("1- Numero bcp = " + listBpc.size());
        if (memoriaVirtual.isEmpty()) {
            return memoriaPrincipal ;
        }else{
            System.out.println("2 - Numero de archivos en principal = " + memoriaPrincipal.size());
            System.out.println("3 - Numero de archivos en virtual = " + memoriaVirtual.size()); 
            listBpc = memoriaPrincipal;
            System.out.println("4- Numero bcp = " + listBpc.size());
                       
            for(int i = 0; i<memoriaVirtual.size(); i++){
                listBpc.add(memoriaVirtual.get(i));
            }
            System.out.println("5- Numero bpc luego del for = " + listBpc.size());
            return listBpc;
        }
        
    }
    
    public static void agregarAColaEspera(String proceso){
        colaEspera.add(proceso);
    }
    
    public static void eliminarProcesoColaEspera(String proceso){
        colaEspera.remove(proceso);
    }
    
    public static void disminuirTamanioUnidad(int tamanioCodASM, int memoria){
        
        if (memoria == 1){
            tamanioPrincipal -= 0.488 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }else{
            tamanioVirtual -= 0.488 + (tamanioCodASM / 1024.0);//lista de archivos bcp
        }
        
        tamanioUnidad -= 0.100 + (tamanioCodASM / 1024.0);
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
}