package Model.Archive;

import java.io.File;

/**
 * ASMFile és una classe que estén de File i implementa la interfície Archive.
 * Permet el tractament i diferenciació dels arxius de tipus codi de baix nivell de llenguatge assembler.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class ASMFile extends File implements Archive{

    /**
     * Constructor de la classe.
     * @param file File del fitxer ASMFile a instanciar.
     */
    public ASMFile(File file) {
        super(file.getPath());
    }
}
