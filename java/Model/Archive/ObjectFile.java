package Model.Archive;

import java.io.File;

public class ObjectFile extends File implements Archive{

    public ObjectFile(File file) {
        super(file.getPath());
    }
}
