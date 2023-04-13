package Controler;

import java.util.ArrayList;

/**
 *
 * @author Alina
 */
public class FilesASM {
    private String nom;
    private ArrayList<String> content;
    private int pos;

    public FilesASM(String nombre, ArrayList<String> contenido, int pos) {
        this.nom = nombre;
        this.content = contenido;
        this.pos = pos;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<String> getContent() {
        return content;
    }
    
    public int getPos(){
        return pos;
    }
}
