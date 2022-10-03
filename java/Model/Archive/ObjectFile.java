package Model.Archive;

import java.io.File;

/**
 * ObjectFile és una classe que estén de File i implementa la interfície Archive.
 * Permet el tractament i diferenciació dels arxius de tipus binari objecte.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class ObjectFile extends File implements Archive{

    /**
     * Constructor de la classe.
     * @param file File del fitxer ObjectFile a instanciar.
     */
    public ObjectFile(File file) {
        super(file.getPath());
    }
}
