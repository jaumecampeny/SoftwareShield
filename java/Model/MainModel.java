package Model;

import static Model.Settings.*;

public class MainModel {
    public enum OS{
        Windows_OS, Linux_OS, Mac_OS, Other_OS
    }
    private final OS os;
    private final FileModel fileModel;
    private final TechniqueModel techniqueModel;


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

    public FileModel getFileModel(){
        return fileModel;
    }

    public TechniqueModel getTechniqueModel(){
        return techniqueModel;
    }

    public OS getOS(){
        return os;
    }
}
