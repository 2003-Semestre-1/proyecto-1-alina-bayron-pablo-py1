
package Controler;

/**
 *
 * @author Alina
 */
public class FilesState {
    private String nomFile;
    private String state;

    public FilesState(String nomFile, String state) {
        this.nomFile = nomFile;
        this.state = state;
    }

    public String getNomFile() {
        return nomFile;
    }

    public String getState() {
        return state;
    }
}
