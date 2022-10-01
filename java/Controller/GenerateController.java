package Controller;

import Model.Archive.*;

import java.io.IOException;

public class GenerateController {
    private final TechniqueController techniqueController;
    private final FileController fileController;
    private final ViewController viewController;

    public GenerateController(TechniqueController techniqueController, ViewController viewController, FileController fileController){
        this.techniqueController = techniqueController;
        this.viewController = viewController;
        this.fileController = fileController;
    }

    public void setGenerateOptions(){
        if(techniqueController.isTechniqueSRS_Selected()){
            viewController.disableButton_CF();
            viewController.disableButton_BN();
        }else if(techniqueController.isTechniqueEW_Selected() || techniqueController.isTechniqueHE_Selected()){
            viewController.disableButton_CF();
            viewController.disableButton_BN();
            viewController.disableButton_BO();
        }else{
            viewController.enableAllGenerations();
        }
    }

    public void generate_CF(){
        try {
            CFile copiaCFile = (CFile) fileController.readInputFile();
            techniqueController.applyCFTechniques(copiaCFile);
            fileController.createCOutputFile(copiaCFile);
            if (!copiaCFile.delete()) System.out.println("ERROR: " + copiaCFile.getName() + " can't be deleted.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate_BN(){
        try {
            if(fileController.isInputCFile()){
                CFile copiaCFile = (CFile) fileController.readInputFile();
                techniqueController.applyCFTechniques(copiaCFile);
                ASMFile asmFile = fileController.createASMOutputFile(copiaCFile);
                if (!copiaCFile.delete()) System.out.println("ERROR: " + copiaCFile.getName() + " can't be deleted.");
                techniqueController.applyBNTechniques(asmFile);
                fileController.createASMOutputFile(asmFile);
                if (!asmFile.delete()) System.out.println("ERROR: " + asmFile.getName() + " can't be deleted.");
            }else{
                ASMFile copiaASMFile = (ASMFile) fileController.readInputFile();
                techniqueController.applyBNTechniques(copiaASMFile);
                fileController.createASMOutputFile(copiaASMFile);
                if (!copiaASMFile.delete()) System.out.println("ERROR: " + copiaASMFile.getName() + " can't be deleted.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void generate_BO(){
        try {
            if(fileController.isInputCFile()){
                CFile copiaCFile = (CFile) fileController.readInputFile();
                techniqueController.applyCFTechniques(copiaCFile);
                fileController.createCOutputFile(copiaCFile);
                ASMFile asmFile = fileController.createASMOutputFile(copiaCFile);
                if (!copiaCFile.delete()) System.out.println("ERROR: " + copiaCFile.getName() + " can't be deleted.");
                techniqueController.applyBNTechniques(asmFile);
                fileController.createASMOutputFile(asmFile);
                ObjectFile objectFile = fileController.createObjectOutputFile(asmFile);
                if (!asmFile.delete()) System.out.println("ERROR: " + asmFile.getName() + " can't be deleted.");
                techniqueController.applyBOTechniques(objectFile);
                fileController.createObjectOutputFile(objectFile);
                if (!objectFile.delete()) System.out.println("ERROR: " + objectFile.getName() + " can't be deleted.");
            }else if(fileController.isInputASMFile()){
                ASMFile copiaASMFile = (ASMFile) fileController.readInputFile();
                techniqueController.applyBNTechniques(copiaASMFile);
                fileController.createASMOutputFile(copiaASMFile);
                ObjectFile objectFile = fileController.createObjectOutputFile(copiaASMFile);
                if (!copiaASMFile.delete()) System.out.println("ERROR: " + copiaASMFile.getName() + " can't be deleted.");
                techniqueController.applyBOTechniques(objectFile);
                fileController.createObjectOutputFile(objectFile);
                if (!objectFile.delete()) System.out.println("ERROR: " + objectFile.getName() + " can't be deleted.");
            }else{
                ObjectFile copiaObjectFile = (ObjectFile) fileController.readInputFile();
                techniqueController.applyBOTechniques(copiaObjectFile);
                fileController.createObjectOutputFile(copiaObjectFile);
                if (!copiaObjectFile.delete()) System.out.println("ERROR: " + copiaObjectFile.getName() + " can't be deleted.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate_BE(){
        try {
            if(fileController.isInputCFile()){
                CFile copiaCFile = (CFile) fileController.readInputFile();
                techniqueController.applyCFTechniques(copiaCFile);
                fileController.createCOutputFile(copiaCFile);
                ASMFile asmFile = fileController.createASMOutputFile(copiaCFile);
                if (!copiaCFile.delete()) System.out.println("ERROR: " + copiaCFile.getName() + " can't be deleted.");
                techniqueController.applyBNTechniques(asmFile);
                fileController.createASMOutputFile(asmFile);
                ObjectFile objectFile = fileController.createObjectOutputFile(asmFile);
                if (!asmFile.delete()) System.out.println("ERROR: " + asmFile.getName() + " can't be deleted.");
                techniqueController.applyBOTechniques(objectFile);
                fileController.createObjectOutputFile(objectFile);
                PEFile peFile = fileController.createExecutableOutputFile(objectFile);
                if (!objectFile.delete()) System.out.println("ERROR: " + objectFile.getName() + " can't be deleted.");
                techniqueController.applyBETechniques(peFile);
                fileController.createExecutableOutputFile(peFile);
                if (!peFile.delete()) System.out.println("ERROR: " + peFile.getName() + " can't be deleted.");
            }else if(fileController.isInputASMFile()){
                ASMFile copiaASMFile = (ASMFile) fileController.readInputFile();
                techniqueController.applyBNTechniques(copiaASMFile);
                fileController.createASMOutputFile(copiaASMFile);
                ObjectFile objectFile = fileController.createObjectOutputFile(copiaASMFile);
                if (!copiaASMFile.delete()) System.out.println("ERROR: " + copiaASMFile.getName() + " can't be deleted.");
                techniqueController.applyBOTechniques(objectFile);
                fileController.createObjectOutputFile(objectFile);
                PEFile peFile = fileController.createExecutableOutputFile(objectFile);
                if (!objectFile.delete()) System.out.println("ERROR: " + objectFile.getName() + " can't be deleted.");
                techniqueController.applyBETechniques(peFile);
                fileController.createExecutableOutputFile(peFile);
                if (!peFile.delete()) System.out.println("ERROR: " + peFile.getName() + " can't be deleted.");
            }else if(fileController.isInputObjectFile()){
                ObjectFile copiaObjectFile = (ObjectFile) fileController.readInputFile();
                techniqueController.applyBOTechniques(copiaObjectFile);
                fileController.createObjectOutputFile(copiaObjectFile);
                PEFile peFile = fileController.createExecutableOutputFile(copiaObjectFile);
                if (!copiaObjectFile.delete()) System.out.println("ERROR: " + copiaObjectFile.getName() + " can't be deleted.");
                techniqueController.applyBETechniques(peFile);
                fileController.createExecutableOutputFile(peFile);
                if (!peFile.delete()) System.out.println("ERROR: " + peFile.getName() + " can't be deleted.");
            }else{
                PEFile copiaPEFile = (PEFile) fileController.readInputFile();
                techniqueController.applyBETechniques(copiaPEFile);
                fileController.createExecutableOutputFile(copiaPEFile);
                if (!copiaPEFile.delete()) System.out.println("ERROR: " + copiaPEFile.getName() + " can't be deleted.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
