package Model.Archive;

import java.io.File;

public class ASMFile extends File implements Archive{

    public ASMFile(File file) {
        super(file.getPath());
    }
}
