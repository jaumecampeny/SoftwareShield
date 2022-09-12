package Controller;

import Model.Archive.*;
import Model.FileModel;
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

public class TechniqueController {
    private final FileController fileController;
    private final ViewController viewController;
    private final TechniqueModel techniqueModel;
    private final MainModel.OS os;

    public TechniqueController(FileController fileController, ViewController viewController, TechniqueModel techniqueModel, MainModel.OS os) {
        this.viewController = viewController;
        this.fileController = fileController;
        this.techniqueModel = techniqueModel;
        this.os = os;
    }

    public void setTechniqueOptions() {
        viewController.disableAllTechniques();
        if (fileController.isInputCFile()) {
            viewController.enableTechnique_MBHS();
            viewController.enableTechnique_SRS();
            viewController.enableTechnique_HE();
            viewController.enableTechnique_SBD();
            if (os == MainModel.OS.Linux_OS || os == MainModel.OS.Mac_OS) viewController.enableTechnique_PTDAW();
            if (os == MainModel.OS.Windows_OS) viewController.enableTechnique_CRDP();

            viewController.enableAllGenerations();

        } else if (fileController.isInputASMFile()) {
            viewController.enableTechnique_MBHS();
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
            viewController.enableTechnique_MBHS();
            viewController.enableTechnique_SRS();
            viewController.enableTechnique_HE();

            viewController.enableAllGenerations();
            viewController.disableButton_CF();
            viewController.disableButton_BN();

        } else if (fileController.isInputPEFile()) {
            viewController.enableTechnique_MBHS();
            viewController.enableTechnique_HE();
            viewController.enableTechnique_SRS();

            viewController.enableAllGenerations();
            viewController.disableButton_CF();
            viewController.disableButton_BN();
            viewController.disableButton_BO();
        }
    }

    public void setTechniqueSelected(String flag, boolean selected) {
        switch (flag) {
            case TAG_TECHNIQUE_MBHS ->
                    techniqueModel.setTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_MBHS, selected);
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

    public boolean isTechniqueMBHS_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_MBHS);
    }

    public boolean isTechniqueSRS_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SRS);
    }

    public boolean isTechniqueHE_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_HE);
    }

    public boolean isTechniqueSBD_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_SBD);
    }

    public boolean isTechniquePTDAW_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_PTDAW);
    }

    public boolean isTechniqueCRDP_Selected() {
        return techniqueModel.getTechniqueSelected(TechniqueModel.Technique.TECHNIQUE_CRDP);
    }

    public void applyCFTechniques(CFile cFile) {
        if (isTechniqueCRDP_Selected()) applyCRDP_Technique(cFile);
        if (isTechniquePTDAW_Selected()) {
            if (os == MainModel.OS.Mac_OS) applyPTDAW_Technique_Mac(cFile);
            if (os == MainModel.OS.Linux_OS) applyPTDAW_Technique_Linux(cFile);
        }
        if (isTechniqueSBD_Selected()) applySBD_Technique(cFile);
    }

    public void applyBNTechniques(ASMFile asmFile) {
        //TODO: Escriure tècniques CF també per ASM
    }

    public void applyBOTechniques(ObjectFile objectFile) {
        //TODO: Escriure tècnica HE també en BO
        if (isTechniqueSRS_Selected()) applySRS_Technique(objectFile);
    }

    public void applyBETechniques(PEFile peFile) {
        if (isTechniqueHE_Selected()) applyHE_Technique(peFile);
        if (isTechniqueMBHS_Selected()) applyMBHS_Technique(peFile);
        if (isTechniqueSRS_Selected()) applySRS_Technique(peFile);
    }

    private void applyCRDP_Technique(CFile cFile) {
        if (!cFile.existsInclude("windows.h")) {
            cFile.addTextToFile(TECHNIQUE_CRDP_SCRIPT1, 1);
            cFile.addInclude("windows.h", 1);
        }
        cFile.addTextToFile(TECHNIQUE_CRDP_SCRIPT2, cFile.getFunctionLine("main") + 1);
    }

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

    private void applyPTDAW_Technique_Linux(CFile cFile) {
        if (!cFile.existsInclude("sys/ptrace.h")) {
            cFile.addTextToFile("#include <sys/ptrace.h>", 1);
            cFile.addInclude("sys/ptrace.h", 1);
        }
        cFile.addTextToFile(TECHNIQUE_PTDAW_LINUX_SCRIPT, cFile.getFunctionLine("main") + 1);
    }

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
        }
    }

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

    private void applyHE_Technique(PEFile peFile) {
        for (int i = 0; i < peFile.getTotalSections(); i++) {
            if (peFile.getSection(i).getName().compareTo(".text") == 0 && peFile.getSection(i).getCharacteristics().contains(SectionFlag.WRITE)) {
                peFile.getSection(i).discardFlag(SectionFlag.WRITE);
                /*
                    //PASSFILE -> binary file path
                    RandomAccessFile raf = new RandomAccessFile(PASSFILE, "rw");

                    //Entrie = Array number to modify
                    int offset = (entrie * 128 );

                    raf.seek(offset);
                    // cipherText = 128 bytes of the new array for the speficic entrie
                    raf.write(cipherText);
                    raf.close();
                 */
            }
        }
    }

    private void applyMBHS_Technique(PEFile peFile) {
        try {
            File tempFile = new File(FilenameUtils.removeExtension(peFile.getAbsoluteFile().toPath() +
                    FileModel.TEMP_EXTENSION + FileModel.EXE_EXTENSION));
            Files.copy(peFile.getAbsoluteFile().toPath(), tempFile.getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            if(!peFile.delete()) System.out.println("ERROR DELETING FILE: " + peFile.getName());
            ProcessBuilder builder = new ProcessBuilder("python", "MBHS_Packer.py", tempFile.getAbsoluteFile().getPath(),
                    "-o", peFile.getAbsoluteFile().getPath()).inheritIO();
            builder.redirectErrorStream(true);
            builder.directory(new File(peFile.getParentFile() + "/MBHS"));
            builder.start().waitFor();
            if(!tempFile.delete()) System.out.println("ERROR DELETING FILE: " + tempFile.getName());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
