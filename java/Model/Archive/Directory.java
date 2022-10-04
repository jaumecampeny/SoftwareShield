package Model.Archive;

import java.io.File;

/**
 * ASMFile és una classe que estén de File i implementa la interfície Archive.
 * Permet el tractament i diferenciació dels directoris.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class Directory extends File implements Archive{

    /**
     * Constructor de la classe.
     * @param file File del directori Directory a instanciar.
     */
    public Directory(File file) {
        super(file.getPath());
    }
}
