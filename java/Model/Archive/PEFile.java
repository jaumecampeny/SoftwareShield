package Model.Archive;

import net.jsign.pe.Section;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * PEFile és una classe que estén de File i implementa la interfície Archive.
 * Permet el tractament i diferenciació dels arxius de tipus binari executable.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class PEFile extends net.jsign.pe.PEFile implements Archive {
    private final File file;
    private final List<Section> sections;

    /**
     * Constructor de la classe.
     * @param file File del fitxer PEFile a instanciar.
     * @throws IOException Excepció originada a causa de no trobar el fitxer.
     */
    public PEFile(File file) throws IOException {
        super(file);
        this.file = file;
        this.sections = this.getSections();
    }

    /**
     * Getter del nom del fitxer/directori
     * @return String amb el nom del fitxer/directori
     */
    @Override
    public String getName() {
        return file.getName();
    }

    /**
     * Getter de la ruta completa del fitxer/directori
     * @return String amb la ruta completa del fitxer/directori
     */
    @Override
    public File getAbsoluteFile() {
        return file.getAbsoluteFile();
    }

    /**
     * Funció que efectua l'eliminació del fitxer/directori en qüestió.
     * @return boolean indicant amb True si el fitxer/directori s'ha eliminat correctament, o False en cas contrari.
     */
    @Override
    public boolean delete() {
        return file.delete();
    }

    /**
     * Getter de les seccions totals que conté el fitxer binari PEFile
     * @return int amb el nombre de seccions totals.
     */
    public int getTotalSections(){
        return sections.size();
    }

    /**
     * Getter de la secció pertinent a l'índex especificat per sectionIndex.
     * @param sectionIndex int amb l'índex de la secció a consultar.
     * @return Section de la secció corresponent a l'índex especificat.
     */
    public Section getSection(int sectionIndex){
        return sections.get(sectionIndex);
    }

    /**
     * Getter del directori pare del fitxer/directori en qüestió.
     * @return File indicant el directori pare del fitxer/directori en qüestió
     */
    public File getParentFile(){
        return this.file.getParentFile();
    }
}
