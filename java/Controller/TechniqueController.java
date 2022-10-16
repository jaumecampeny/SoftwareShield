package Controller;

import Model.Archive.*;
import Model.Archive.ArchiveModel;
import Model.MainModel;
import Model.TechniqueModel;
import net.jsign.pe.SectionFlag;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static Model.Settings.*;
import static Model.TechniqueModel.*;

/**
 * TechniqueController és una classe pròpia del mòdul Controlador, seguint el patró MVC.
 * S'encarrega de la lògica pertinent a l'habilitació i inhabilitació de les tècniques en la vista segons convingui, d'afegit a la seva aplicació en el transcurs del procés de creació del fitxer de sortida.
 * Disposa del seu propi model i de comunicacions tant amb el controlador FileController per a l'escriputra i modificació dels fitxers a generació, així com amb el controlador ViewController per a tractar l'habilitació dels elements pertinents al panell de tècniques.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class TechniqueController {
    private final FileController fileController;
    private final ViewController viewController;
    private final TechniqueModel techniqueModel;
    private final MainModel.OS os;

    /**
     * Únic constructor de la classe. Permet la creació de la instància TechniqueController, mitjançant l'enregistrament de les instàncies rebudes TechniqueModel pertinent al Model, i FileController i ViewController pertinents als controladors necessaris.
     * @param fileController FileController com a controlador que permetrà la modificació del fitxer de sortida a l'hora d'aplicar les tècniques seleccionades en aquests.
     * @param viewController ViewController com a controlador que permetrà la manipulació lògica de l'habilitació de les diverses tècniques dins del respectiu panell en la vista.
     * @param techniqueModel TechniqueModel com a model que disposa dels canvis a realitzar quant a l'aplicació de les diverses tècniques es refereix, d'afegit a l'enregistrament de quines tècniques ha seleccionat l'usuari i quines no.
     * @param os OS indicant el sistema operatiu en el que s'executa el programa.
     */
    public TechniqueController(FileController fileController, ViewController viewController, TechniqueModel techniqueModel, MainModel.OS os) {
        this.viewController = viewController;
        this.fileController = fileController;
        this.techniqueModel = techniqueModel;
        this.os = os;
    }

    /**
     * Mètode que defineix la lògica d'habilitació en la interfície visual, de les possibles tècniques a aplicar segons el fitxer d'entrada introduït per l'usuari.
     */
    public void setTechniqueOptions() {
        viewController.disableAllTechniques();
        if (fileController.isInputCFile()) {
            viewController.enableTechnique_EW();
            viewController.enableTechnique_SRS();
            viewController.enableTechnique_HE();
            viewController.enableTechnique_SBD();
            if (os == MainModel.OS.Linux_OS || os == MainModel.OS.Mac_OS) viewController.enableTechnique_PTDAW();
            if (os == MainModel.OS.Windows_OS) viewController.enableTechnique_CRDP();
            viewController.enableAllGenerations();

        } else if (fileController.isInputASMFile()) {
            viewController.enableTechnique_EW();
            viewController.enableTechnique_SRS();
            viewController.enableTechnique_HE();
            viewController.enableTechnique_SBD();
            viewController.enableTechnique_SBD();
            if (os == MainModel.OS.Linux_OS || os == MainModel.OS.Mac_OS)
                viewController.enableTechnique_PTDAW();
            if (os == MainModel.OS.Windows_OS) viewController.enableTechnique_CRDP();
            viewController.enableAllGenerations();
            viewController.disableButton_CF();

        } else if (fileController.isInputObjectFile()) {
            viewController.enableTechnique_EW();
            viewController.enableTechnique_SRS();
            viewController.enableTechnique_HE();
            viewController.enableAllGenerations();
            viewController.disableButton_CF();
            viewController.disableButton_BN();

        } else if (fileController.isInputPEFile()) {
            viewController.enableTechnique_EW();
            viewController.enableTechnique_HE();
            viewController.enableTechnique_SRS();
            viewController.enableAllGenerations();
            viewController.disableButton_CF();
            viewController.disableButton_BN();
            viewController.disableButton_BO();
        }
    }

    /**
     * Mètode que actualitza el model amb el canvi realitzat a la tècnica identificada amb flag, i el nou estat identificat amb selected.
     * @param flag String que indica l'identificador de la tècnica actualitzada.
     * @param selected boolean que indica el nou estat de la tècnica identificada amb flag.
     */
    public void setTechniqueSelected(String flag, boolean selected) {
        switch (flag) {
            case TAG_TECHNIQUE_EW ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_EW, selected);
            case TAG_TECHNIQUE_SRS ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SRS, selected);
            case TAG_TECHNIQUE_HE ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_HE, selected);
            case TAG_TECHNIQUE_SBD ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SBD, selected);
            case TAG_TECHNIQUE_PTDAW ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_PTDAW, selected);
            case TAG_TECHNIQUE_CRDP ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_CRDP, selected);
        }
    }

    /**
     * Getter de l'estat de la tècnica Encryption Wrappers.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniqueEW_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_EW);
    }

    /**
     * Getter de l'estat de la tècnica Stripping Redundant Symbols.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniqueSRS_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SRS);
    }

    /**
     * Getter de l'estat de la tècnica Header Entrypoint.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniqueHE_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_HE);
    }

    /**
     * Getter de l'estat de la tècnica Software Breakpoint Detection.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniqueSBD_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SBD);
    }

    /**
     * Getter de l'estat de la tècnica PT_DenyAttachWorked.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniquePTDAW_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_PTDAW);
    }

    /**
     * Getter de l'estat de la tècnica CheckRemoteDebuggerPresent.
     * @return boolean que indica amb True si la tècnica està seleccionada, o amb False en cas contrari.
     */
    public boolean isTechniqueCRDP_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_CRDP);
    }

    /**
     * Mètode que realitza la lògica pertinent per a l'aplicació de les tècniques seleccionades aplicables en fase de codi font.
     * @param cFile CFile pertinent al fitxer on aplicar les tècniques seleccionades.
     */
    public void applyCFTechniques(CFile cFile) {
        if (isTechniqueCRDP_Selected()) applyCRDP_Technique(cFile);
        if (isTechniquePTDAW_Selected()) {
            if (os == MainModel.OS.Mac_OS) applyPTDAW_Technique_Mac(cFile);
            if (os == MainModel.OS.Linux_OS) applyPTDAW_Technique_Linux(cFile);
        }
        if (isTechniqueSBD_Selected()) applySBD_Technique(cFile);
    }

    /**
     * Mètode que realitza la lògica pertinent per a l'aplicació de les tècniques seleccionades aplicables en fase de codi de baix nivell.
     * @param asmFile ASMFile pertinent al fitxer on aplicar les tècniques seleccionades.
     */
    public void applyBNTechniques(ASMFile asmFile) {
        //TODO: Escriure tècniques CF també per ASM
    }

    /**
     * Mètode que realitza la lògica pertinent per a l'aplicació de les tècniques seleccionades aplicables en fase de binari de tipus objecte.
     * @param objectFile ObjectFile pertinent al fitxer on aplicar les tècniques seleccionades.
     */
    public void applyBOTechniques(ObjectFile objectFile) {
        //TODO: Escriure tècnica HE també en BO
        if (isTechniqueSRS_Selected()) applySRS_Technique(objectFile);
    }

    /**
     * Mètode que realitza la lògica pertinent per a l'aplicació de les tècniques seleccionades aplicables en fase de binari de tipus executable.
     * @param peFile PEFile pertinent al fitxer on aplicar les tècniques seleccionades.
     */
    public void applyBETechniques(PEFile peFile) throws IOException {
        if (isTechniqueHE_Selected()) applyHE_Technique(peFile);
        if (isTechniqueEW_Selected()){
            CFile cfile = fileController.copyUnpackFile();
            cfile.readFile();
            if(isTechniqueCRDP_Selected()) applyCRDP_Technique(cfile);
            if(isTechniqueSBD_Selected()) applySBD_Technique(cfile);
            if (isTechniquePTDAW_Selected()) {
                if (os == MainModel.OS.Mac_OS) applyPTDAW_Technique_Mac(cfile);
                if (os == MainModel.OS.Linux_OS) applyPTDAW_Technique_Linux(cfile);
            }
            applyEW_Technique(peFile);
        }
        if (isTechniqueSRS_Selected()) applySRS_Technique(peFile);
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica CheckRemoteDebuggerPresent en el cFile indicat com a paràmetre.
     * @param cFile CFile pertinent al fitxer on aplicar la tècnica CheckRemoteDebuggerPresent.
     */
    private void applyCRDP_Technique(CFile cFile) {
        if (!cFile.existsInclude("windows.h")) {
            cFile.addTextToFile(TECHNIQUE_CRDP_SCRIPT1, 1);
            cFile.addInclude("windows.h", 1);
        }
        if (!cFile.existsInclude("stdbool.h")) {
            cFile.addTextToFile(TECHNIQUE_CRDP_SCRIPT2, 1);
            cFile.addInclude("stdbool.h", 1);
        }

        int functionLine = cFile.getFunctionLine("_start") != -1 ? cFile.getFunctionLine("_start") : cFile.getFunctionLine("main");
        cFile.addTextToFile(TECHNIQUE_CRDP_SCRIPT3, functionLine + 1);
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica PT_DenyAttachWorked en el cFile indicat com a paràmetre, per a sistemes operatius MAC.
     * @param cFile CFile pertinent al fitxer on aplicar la tècnica PT_DenyAttachWorked.
     */
    private void applyPTDAW_Technique_Mac(CFile cFile) {
        if (!cFile.existsInclude("stdlib.h")) {
            cFile.addTextToFile("#include <stdlib.h>", 1);
            cFile.addInclude("stdlib.h", 1);
        }
        if (!cFile.existsInclude("sys/types.h")) {
            cFile.addTextToFile("#include <sys/types.h>", 1);
            cFile.addInclude("sys/types.h", 1);
        }
        if (!cFile.existsInclude("sys/ptrace.h")) {
            cFile.addTextToFile("#include <sys/ptrace.h>", 1);
            cFile.addInclude("sys/ptrace.h", 1);
        }
        if (!cFile.existsInclude("unistd.h")) {
            cFile.addTextToFile("#include <unistd.h>", 1);
            cFile.addInclude("unistd.h", 1);
        }

        cFile.addTextToFile(TECHNIQUE_PTDAW_MAC_SCRIPT1, cFile.getLastIncludeLine() + 1);
        cFile.addFunction("sigsegv_handler", cFile.getLastIncludeLine() + 2);

        cFile.addTextToFile(TECHNIQUE_PTDAW_MAC_SCRIPT2, cFile.getFunctionLine("main") + 1);
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica PT_DenyAttachWorked en el cFile indicat com a paràmetre, per a sistemes operatius Linux.
     * @param cFile CFile pertinent al fitxer on aplicar la tècnica PT_DenyAttachWorked.
     */
    private void applyPTDAW_Technique_Linux(CFile cFile) {
        if (!cFile.existsInclude("sys/ptrace.h")) {
            cFile.addTextToFile("#include <sys/ptrace.h>", 1);
            cFile.addInclude("sys/ptrace.h", 1);
        }
        cFile.addTextToFile(TECHNIQUE_PTDAW_LINUX_SCRIPT, cFile.getFunctionLine("main") + 1);
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica Software Breakpoint Detection en el cFile indicat com a paràmetre.
     * @param cFile CFile pertinent al fitxer on aplicar la tècnica Software Breakpoint Detection.
     */
    private void applySBD_Technique(CFile cFile) {
        StringBuilder functionsComparison = new StringBuilder();
        if (cFile.getFunctionsSize() - 1 > 0) {
            cFile.addTextToFile(TECHNIQUE_SBD_SCRIPT1, 1);

            if (cFile.getFunction(0).getFunctionName().compareTo("main") != 0) {
                functionsComparison.append(String.format(TECHNIQUE_SBD_SCRIPT5, cFile.getFunction(0).getFunctionName()));
                cFile.addTextToFile(String.format(TECHNIQUE_SBD_SCRIPT2, cFile.getFunction(0).getFunctionName()),
                        cFile.getFunction(0).getDeclaredLine() + 1);
                cFile.addTextToFile(String.format(TECHNIQUE_SBD_SCRIPT3, cFile.getFunction(0).getFunctionName()),
                        cFile.getFunctionLine("main"));
            }


            for (int i = 1; i < cFile.getFunctionsSize() - 1; i++) {
                if (cFile.getFunction(i).getFunctionName().compareTo("main") == 0) continue;
                if (!functionsComparison.isEmpty()) functionsComparison.append(" || ");
                functionsComparison.append(String.format(TECHNIQUE_SBD_SCRIPT5, cFile.getFunction(i).getFunctionName()));
                cFile.addTextToFile(String.format(TECHNIQUE_SBD_SCRIPT2, cFile.getFunction(i).getFunctionName()),
                        cFile.getFunction(i).getDeclaredLine() + 1);
                cFile.addTextToFile(String.format(TECHNIQUE_SBD_SCRIPT3, cFile.getFunction(i).getFunctionName()),
                        cFile.getFunctionLine("main"));
            }
            cFile.addTextToFile(String.format(TECHNIQUE_SBD_SCRIPT4, functionsComparison), cFile.getFunctionLine("main") + 1);
        }else{
            viewController.showInfoMessage("Tècnica Software Breakpoint Detection no aplicada", "Degut a no considerar múltiples declaracions de funcions en el fitxer, no ha estat possible aplicar la tècnica.");
        }
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica Stripping Redundant Symbols en el archive indicat com a paràmetre.
     * @param archive Archive pertinent al fitxer on aplicar la tècnica Stripping Redundant Symbols.
     */
    private void applySRS_Technique(Archive archive) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "strip", archive.getName());
            builder.redirectErrorStream(true).inheritIO();
            builder.directory(archive.getParentFile());
            builder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica Header Entrypoint en el peFile indicat com a paràmetre.
     * @param peFile PEFile pertinent al fitxer on aplicar la tècnica Header Entrypoint.
     */
    private void applyHE_Technique(PEFile peFile) {
        for (int i = 0; i < peFile.getTotalSections(); i++) {
            if (peFile.getSection(i).getName().compareTo(".text") == 0 && peFile.getSection(i).getCharacteristics().contains(SectionFlag.WRITE)) {
                peFile.getSection(i).discardFlag(SectionFlag.WRITE);
            }
        }
    }

    /**
     * Mètode que realitza la lògica pertinent a l'aplicació de la tècnica Encryption Wrapper en el peFile indicat com a paràmetre.
     * @param peFile PEFile pertinent al fitxer on aplicar la tècnica Encryption Wrapper.
     */
    private void applyEW_Technique(PEFile peFile) {
        try {
            File tempFile = new File(FilenameUtils.removeExtension(peFile.getAbsoluteFile().toPath() +
                    ArchiveModel.TEMP_EXTENSION + ArchiveModel.EXE_EXTENSION));
            Files.copy(peFile.getAbsoluteFile().toPath(), tempFile.getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            if(!peFile.delete()) System.out.println("ERROR DELETING FILE: " + peFile.getName());
            ProcessBuilder builder = new ProcessBuilder("python", fileController.getEWPath() + "/EW_Packer.py", tempFile.getAbsoluteFile().getPath(),
                    "-o", peFile.getAbsoluteFile().getPath()).inheritIO();
            builder.redirectErrorStream(true);
            builder.directory(new File(fileController.getEWPath()));
            builder.start().waitFor();
            if(!tempFile.delete()) System.out.println("ERROR DELETING FILE: " + tempFile.getName());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
