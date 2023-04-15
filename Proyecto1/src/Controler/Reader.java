package Controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import Controler.Controller;
import Visual.InterfaceMensajes;
import javax.swing.JOptionPane;


/**
 *
 * @author Alina
 */
public class Reader {
    public static ArrayList<FilesASM> waitingList = new ArrayList<>();//Lista de procesos en espera
    public static ArrayList<FilesASM> processExecuted = new ArrayList<>();//Lista de procesos ejecutados
    public static ArrayList<FilesState> filesErrors = new ArrayList<>();//Lista de los archivos que presentan algun error 
    public static int pos = 0;
    public static int posicionMemoria = 0;
    public static int cant_lines = 0;
    
    //Se cargan los archivos correctos en la lista de espera
    public static void fileUpload (JFileChooser chooser){
        File[] files = chooser.getSelectedFiles();
        for(int i = 0; i<files.length; i++){
            ArrayList<String> listTemp = new ArrayList<>();
            listTemp = readFile (files[i]);
            if (!listTemp.isEmpty()){
                FilesASM nodo = new FilesASM(files[i].getName(),readFile(files[i]), i);
                waitingList.add(nodo); //Se agrega a la cola de espera
            }
        }
        int i = 0;
        while (i< waitingList.size()){
            Controller.newBPC(waitingList.get(i).getNom(), waitingList.get(i).getContent(), posicionMemoria);
            i++;
            posicionMemoria++;
        }
        waitingList.clear();
        if (filesErrors.size() != 0){
            System.out.println("Si hay archivos malos");
            int x = 0;
            String mensaje = "Se detectaron archivos con algún error de sintaxis.\nLos siguientes archivos no se tomaran en cuenta: ";
            while (x< filesErrors.size()){
                mensaje += 
                            "\nArchivo: " + filesErrors.get(x).getNomFile() + 
                            "\n\n - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - \n" ;
                x++;
            }
            mensaje+= "\n - - - - - - - - - - Fin de archivos dañados - - - - - - - - - - -\n";
            JOptionPane.showMessageDialog(null, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            filesErrors.clear();
        }
    }
    
    //Lectura de archivos .asm
    public static ArrayList<String> readFile (File fileName){
        ArrayList<String> fileLines = new ArrayList<>();
        if (validateFile (fileName)){
            try {
                BufferedReader prompter = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = prompter.readLine()) != null) {
                    if (validateSyntax(line)){
                        fileLines.add(line); //linea leida del archivo
                    }else {
                        fileLines.clear();
                        FilesState nodoError = new FilesState(fileName.getName(),"Error:Problema de sintaxis ");
                        filesErrors.add(nodoError);
                        return fileLines;
                    }
                    cant_lines++;
                }
                prompter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileLines;
    }

    //Toma el primer proceso de la lista de espera y lo pasa a la lista de procesados
    public static  void updateLists(){
        if(!waitingList.isEmpty()){
            FilesASM temp = waitingList.get(0);
            waitingList.remove(0);
            processExecuted.add(temp);
        }
    }
    
    //------------------------------------      FUNCIONES DE VALIDACIONES       ------------------------------------//
    //Verificar que el archivo exista y que no este en blanco
    public static boolean validateFile (File fileName){
        if (!fileName.getName().endsWith(".asm")) {
            FilesState nodoError = new FilesState(fileName.getName(),"Error:El archivo seleccionado no es de tipo .asm");
            filesErrors.add(nodoError);
            return false;
        }
        if (!fileName.exists()) {
            FilesState nodoError = new FilesState(fileName.getName(),"Error:El archivo seleccionado no existe");
            filesErrors.add(nodoError);
            return false;
        } else if (!fileName.isFile()) {
            FilesState nodoError = new FilesState(fileName.getName(),"Error:La ruta no corresponde a un archivo");
            filesErrors.add(nodoError);
            return false;
        } else if (fileName.length() == 0){
            FilesState nodoError = new FilesState(fileName.getName(),"Error:El archivo seleccionado esta vacio");
            filesErrors.add(nodoError);
            return false;
        }  
        return true;
    }
    
    //Verifica que la sintaxis de las instrucciones del archivo sean validas 
    public static  boolean validateSyntax(String instruction) {
        instruction = instruction.toUpperCase();
        instruction = instruction.replace(", ", " ");
        instruction = instruction.replace(",", " ");
        String[] tokens = instruction.split("\\s+");
        
        // Validación de la sintaxis para cada instrucción 
        if (tokens.length == 3 && tokens[0].equals("MOV")){
            if (esRegistro(tokens[1]) && (esOperando(tokens[2]) || esRegistro(tokens[2]))) {
                return true;
            }else if (tokens[1].equals("AH") && esRegistroAH(tokens[2])){
                return true;
            }       
            
        }else if ((tokens[0].equals("ADD")) || (tokens[0].equals("SUB")) 
                || (tokens[0].equals("LOAD"))|| (tokens[0].equals("STORE")) 
                || (tokens[0].equals("PUSH")) || (tokens[0].equals("POP"))){
            if (tokens.length == 2 && esRegistro(tokens[1])){ //&& esOperando(tokens[2])) {
                return true;
            }
        }else if ((tokens[0].equals("INC")) || (tokens[0].equals("DEC"))){
            if ((tokens.length == 1) || (tokens.length == 2 && esRegistro(tokens[1]))){
                return true;
            }
        }else if (tokens.length == 3 && (tokens[0].equals("SWAP") || tokens[0].equals("CMP"))){
            if (esRegistro(tokens[1]) && esRegistro(tokens[2])) {
                return true;
            }         
        }else if (tokens[0].equals("INT") && tokens.length == 2 && 
            (tokens[1].equals("20H") || tokens[1].equals("10H")
            || tokens[1].equals("09H") || tokens[1].equals("21H") )){
            return true;
        }else if (tokens.length == 2 && (tokens[0].equals("JMP") || tokens[0].equals("JE") || tokens[0].equals("JNE"))){
            int num = Integer.parseInt(tokens[1]);
            if (num < 0 || num > 0){
                return true;
            }
        }else if (tokens[0].equals("PARAM")){
            int largo = tokens.length;
            System.out.println("Leyo el param y el largo es: " + largo);
            if((largo == 2||largo ==3||largo == 4)){
                return true;
            }
        }else {
            System.out.println("en el else, Instrucción mala: " + instruction);
            return false;
        }
        System.out.println("en el return, Instrucción mala: " + instruction);
        return false;
    }
    
    // Verifica que el string ingresado sea un registro valido
    private static boolean esRegistroAH(String token) {
        if (token.endsWith(",")) {
            token = token.substring(0, token.length() - 1);
        }
        return token.matches("(3CH|3DH|4DH|40H|41H)");
    }

    // Verifica que el string ingresado sea un registro valido
    private static boolean esRegistro(String token) {
        if (token.endsWith(",")) {
            token = token.substring(0, token.length() - 1);
        }
        return token.matches("(AC|AX|BX|CX|DX|AH)");
    }

    // Verifica que el string es un operando válido (registro o entero negativo o positivo)
    private static boolean esOperando(String token) {
        return token.matches("-?\\d+|\\[(AC|AX|BX|CX|DX|AH)\\]");
    }
    
    // Metodos de get y set
//    public String getLine(){
//        String line;
//        if (pos < cant_lines){
//            line = fileLines.get(pos);
//            pos++;
//            return line;
//        }else{
//            return null;
//        }
//    }
    
    public void setParams(){
        pos = 0;
        cant_lines = 0;
    }
    
    public ArrayList<FilesASM> getWaitingList() {
        return waitingList;
    }
    
    public ArrayList<FilesASM> getProcessExecuted() {
        return processExecuted;
    }
}//fin de la clase
