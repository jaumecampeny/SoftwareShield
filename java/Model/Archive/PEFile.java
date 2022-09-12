package Model.Archive;

import net.jsign.pe.Section;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PEFile extends net.jsign.pe.PEFile implements Archive {
    private final File file;
    private final ArrayList<Section> sections;

    public PEFile(File file) throws IOException {
        super(file);
        this.file = file;
        this.sections = new ArrayList<Section>();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public File getAbsoluteFile() {
        return file.getAbsoluteFile();
    }

    @Override
    public boolean delete() {
        return file.delete();
    }

    public int getTotalSections(){
        return sections.size();
    }

    public Section getSection(int sectionIndex){
        return sections.get(sectionIndex);
    }

    public File getParentFile(){
        return this.file.getParentFile();
    }
}
