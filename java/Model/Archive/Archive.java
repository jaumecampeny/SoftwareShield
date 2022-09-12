package Model.Archive;

import java.io.File;

public interface Archive {

    class Directory extends File implements Archive{
        public Directory(File file) {
            super(file.getPath());
        }
    }

    String getName();
    File getAbsoluteFile();
    boolean delete();
    File getParentFile();
}
