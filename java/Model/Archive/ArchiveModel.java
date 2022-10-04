package Model.Archive;

import Model.Archive.ASMFile;
import Model.Archive.Archive;
import Model.Archive.CFile;
import Model.Archive.ObjectFile;
import Model.Archive.PEFile;

import java.io.File;
import java.io.IOException;

/**
 * ArchiveModel és una classe pròpia del mòdul Model, seguint el patró MVC.
 * Resulta en el model encarregat del tractament i enregistrament de la informació clau pel que fa al fitxer d'entrada, i el posterior procés de creació del fitxer de sortida.
 * Juntament amb la interfície Archive, conforme el model que aglutina la memòria del programa vinculada als fitxers.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class ArchiveModel {
    public static final String INPUT_FILE = "[INPUT]";
    public static final String OUTPUT_FILE = "[OUTPUT]";
    public static final String[] SUPPORTED_EXTENSIONS = {".c", ".s", ".o",".exe"};
    public static final String C_EXTENSION = ".c";
    public static final String ASM_EXTENSION = ".s";
    public static final String OBJECT_EXTENSION = ".o";
    public static final String EXE_EXTENSION = ".exe";
    public static final String TEMP_EXTENSION = ".tmp";

    private Archive inputFile;
    private Archive outputDirectory;

    /**
     * Constructor de la classe.
     */
    public ArchiveModel(){
        this.inputFile = null;
        this.outputDirectory = null;
    }

    /**
     * Mètode que s'encarrega de la lectura del fitxer, i la posterior càrrega d'informació important en memòria. Actualitza en conseqüència el valor de la variable de classe inputFile.
     * D'afegit també permet l'actualització de la variable de classe outputDirectory amb l'especificació de la ruta destí.
     * @param file File amb el fitxer introduït per l'usuari.
     * @param extension String amb l'extensió del fitxer File.
     * @param flag String amb el flag que determina si File pertany al fitxer d'entrada especificat, o a la ruta de sortida del fitxer resultant.
     * @throws IOException Excepció originada a causa de no trobar el fitxer o directori.
     */
    public void loadFile(File file, String extension, String flag) throws IOException {
        switch(flag){
            case INPUT_FILE -> {
                switch(extension){
                    case C_EXTENSION -> inputFile = new CFile(file);
                    case ASM_EXTENSION -> inputFile = new ASMFile(file);
                    case OBJECT_EXTENSION -> inputFile = new ObjectFile(file);
                    case EXE_EXTENSION -> inputFile = new PEFile(file);
                }
            }
            case OUTPUT_FILE -> outputDirectory = new Archive.Directory(file);
        }
    }

    /**
     * Getter del fitxer d'entrada pertinent a la variable de classe inputFile.
     * @return Archive corresponent amb la informació pertinent al fitxer d'entrada.
     */
    public Archive getInputFile() {
        return inputFile;
    }

    /**
     * Getter de la ruta de sortida pertinent a la variable de classe outputDirectory.
     * @return Archive corresponent a la ruta de sortida especificada.
     */
    public Archive getOutputDirectory() {
        return outputDirectory;
    }
}
