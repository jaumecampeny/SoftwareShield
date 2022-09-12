package Model;

import Model.Archive.ASMFile;
import Model.Archive.Archive;
import Model.Archive.CFile;
import Model.Archive.ObjectFile;
import Model.Archive.PEFile;

import java.io.File;
import java.io.IOException;

public class FileModel {
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

    public FileModel(){
        this.inputFile = null;
        this.outputDirectory = null;
    }

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

    public Archive getInputFile() {
        return inputFile;
    }

    public Archive getOutputDirectory() {
        return outputDirectory;
    }
}
