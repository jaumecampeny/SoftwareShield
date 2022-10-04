package Model.Archive;

import java.io.File;

/**
 * Archive és una interfície que ajuda en l'organització i establiment de relacions entre classes de tipus de Fitxers.
 * Aquesta, juntament amb les classes que l'utilitzen i el model ArchiveModel, considerarien la part del mòdul del model pertinent als Fitxers.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public interface Archive {

    class Directory extends File implements Archive{
        public Directory(File file) {
            super(file.getPath());
        }
    }

    /**
     * Getter del nom del fitxer/directori
     * @return String amb el nom del fitxer/directori
     */
    String getName();

    /**
     * Getter de la ruta completa del fitxer/directori
     * @return String amb la ruta completa del fitxer/directori
     */
    File getAbsoluteFile();

    /**
     * Funció que efectua l'eliminació del fitxer/directori en qüestió.
     * @return boolean indicant amb True si el fitxer/directori s'ha eliminat correctament, o False en cas contrari.
     */
    boolean delete();

    /**
     * Getter del directori pare del fitxer/directori en qüestió.
     * @return File indicant el directori pare del fitxer/directori en qüestió
     */
    File getParentFile();
}
