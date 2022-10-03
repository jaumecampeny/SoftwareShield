package Controller;

import Model.Archive.*;

import java.io.IOException;

/**
 * GenerateController és una classe pròpia del mòdul controlador, seguint el patró MVC.
 * S'encarrega de la lògica pertinent als respectius processos de generació del fitxer de sortida.
 * Disposa de comunicació amb la resta de controladors: TechniqueController a l'hora d'aplicar les tècniques en el moment específic dins del procés de generació,
 * FileController per al tractament dels fitxers, i finalment ViewController a l'hora de dur a terme la lògica de l'habilitació dels botons de generació.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class GenerateController {
    private final TechniqueController techniqueController;
    private final FileController fileController;
    private final ViewController viewController;

    /**
     * Únic constructor de la classe. Permet la creació de la instància GenerateController, mitjançant l'enregistrament de les instàncies rebudes TechniqueController, FileController i ViewController del mòdul controlador.
     * @param techniqueController TechniqueController com a controlador que permetrà l'aplicació de les tècniques seleccionades quan es requereixi.
     * @param viewController ViewController com a controlador que permetrà la manipulació de l'habilitació dels diversos botons de generació del fitxer de sortida.
     * @param fileController FileController com a controlador que permetrà la manipulació dels fitxers segons convingui.
     */
    public GenerateController(TechniqueController techniqueController, ViewController viewController, FileController fileController){
        this.techniqueController = techniqueController;
        this.viewController = viewController;
        this.fileController = fileController;
    }

    /**
     * Mètode que defineix la lògica d'habilitació en la interfície visual, de les possibles opcions de generació de fitxer de sortida segons el fitxer d'entrada introduït, així com les tècniques seleccionades.
     */
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

    /**
     * Mètode que realitza la lògica pertinent per a dur a terme la creació del fitxer de sortida en format codi font.
     */
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

    /**
     * Mètode que realitza la lògica pertinent per a dur a terme la creació del fitxer de sortida en format codi de baix nivell.
     */
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

    /**
     * Mètode que realitza la lògica pertinent per a dur a terme la creació del fitxer de sortida en format binari de tipus objecte.
     */
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

    /**
     * Mètode que realitza la lògica pertinent per a dur a terme la creació del fitxer de sortida en format binari de tipus executable.
     */
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
