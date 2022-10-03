package Model;

import static Model.Settings.*;

/**
 * MainModel és una classe pròpia del mòdul Model, seguint el patró MVC.
 * Resulta en la classe principal dins d'aquest mòdul, doncs és la que agrupa la resta de mòduls, considerant així tota la informació dins seu.
 * Disposa del model FileModel, TechniqueModel, com també el sistema operatiu des del que s'executa el programa.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class MainModel {
    public enum OS{
        Windows_OS, Linux_OS, Mac_OS, Other_OS
    }
    private final OS os;
    private final FileModel fileModel;
    private final TechniqueModel techniqueModel;

    /**
     * Constructor de la classe. Permet la creació de la instància MainModel, així com de FileModel i TechniqueModel. Alhora també enregistra l'especificació de sistema operatiu del host.
     * @param os OS pertinent al sistema operatiu de l'entorn on s'executa el programa.
     */
    public MainModel(String os){
        switch (os) {
            case WindowsOS -> this.os = OS.Windows_OS;
            case LinuxOS -> this.os = OS.Linux_OS;
            case MacOS -> this.os = OS.Mac_OS;
            default -> this.os = OS.Other_OS;
        }
        fileModel = new FileModel();
        techniqueModel = new TechniqueModel();
    }

    /**
     * Getter del model de fitxers FileModel
     * @return FileModel respectiu al model de fitxers.
     */
    public FileModel getFileModel(){
        return fileModel;
    }

    /**
     * Getter del model de tècniques TechniqueModel
     * @return TechniqueModel respectiu al model de tècniques.
     */
    public TechniqueModel getTechniqueModel(){
        return techniqueModel;
    }

    /**
     * Getter del tipus de sistema operatiu enregistrat.
     * @return OS del sistema operatiu del host.
     */
    public OS getOS(){
        return os;
    }
}
