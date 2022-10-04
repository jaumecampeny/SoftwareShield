package Controller;

import Model.Archive.*;
import Model.Archive.ArchiveModel;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

import static Model.Archive.ArchiveModel.*;
import static Model.Settings.*;

/**
 * FileController és una classe pròpia del mòdul controlador, seguint el patró MVC.
 * S'encarrega de la lògica pertinent a la lectura, creació i modificació de fitxers.
 * Disposa del seu propi model i de comunicació amb el controlador ViewController a l'hora de configurar les respectives interfícies de selecció de fitxers i directoris, i l'obtenció del fitxer seleccionat per l'usuari com entrada.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class FileController {
    private final ArchiveModel archiveModel;
    private final ViewController viewController;

    /**
     * Únic constructor de la classe. Permet la creació de la instància FileController, mitjançant l'enregistrament de les instàncies rebudes ArchiveModel pertinent al Model i ViewController pertinent als Controladors.
     * @param archiveModel ArchiveModel com a model que permetrà l'enregistrament dels paràmetres dessitjats del fitxer d'entrada, d'afegit a la facilitació en la generació dels fitxers de sortida.
     * @param viewController ViewController com a controlador que permetrà l'obtenció del fitxer d'entrada especificat per l'usuari, d'afegit a la configuració de les respectives interfícies de selecció.
     */
    public FileController(ArchiveModel archiveModel, ViewController viewController) {
        this.archiveModel = archiveModel;
        this.viewController = viewController;
    }

    /**
     * Mètode que aplica la lògica pertinent a la selecció del fitxer d'entrada.
     * Crida la interfície de selecció mitjançant el controlador de la vista, d'afegit a la seva configuració, càrrega del fitxer resultant al model, i respectiva emissió de missatges d'informació o d'error segons correspongui.
     */
    public void actionPerformedInputFile() {
        int result = viewController.createFileDialog(JFileChooser.FILES_ONLY,
                new FileNameExtensionFilter("Codi C, Codi ASM i Binaris objecte i executables", "c", "s", "o", "exe"));


        if (result == JFileChooser.APPROVE_OPTION) {
            File fileName = viewController.getSelectedFile();

            //Comprovar que el fitxer sigui un .c, .asm o .exe.
            Optional<String> extension = Optional.of(fileName.getName())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(fileName.getName().lastIndexOf(".")));

            //Comprovar que el fitxer tingui extensió i que sigui una de les acceptades
            if (extension.isEmpty() || !Arrays.asList(SUPPORTED_EXTENSIONS).contains(extension.get())) {
                viewController.showErrorMessage(ERROR_WRONG_EXTENSION_TITLE, ERROR_WRONG_EXTENSION_MESSAGE);
                return;
            }

            try {
                archiveModel.loadFile(fileName, extension.get(), ArchiveModel.INPUT_FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            viewController.setInputText(fileName.getName());
            viewController.showInfoMessage(INFO_CORRECT_FILE_TITLE, INFO_CORRECT_FILE_MESSAGE);
        }
    }

    /**
     * Mètode que aplica la lògica pertinent a la selecció de la ruta de sortida.
     * Crida la interfície de selecció mitjançant el controlador de la vista, d'afegit a la seva configuració, càrrega la ruta resultant al model, i respectiva emissió de missatges d'informació o d'error segons correspongui.
     */
    public void actionPerformedOutputPath() {
        int result = viewController.createFileDialog(JFileChooser.DIRECTORIES_ONLY, null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = viewController.getSelectedFile();
            try {
                archiveModel.loadFile(directory, null, ArchiveModel.OUTPUT_FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            viewController.setOutputText(directory.getName());
        }
    }

    /**
     * Funció que comprova si el fitxer d'entrada rebut és de tipus PEFile.
     * @return boolean que indica amb True que el fitxer és de tipus PEFile, o amb False en cas contrari.
     */
    public boolean isInputPEFile() {
        return archiveModel.getInputFile() instanceof PEFile;
    }

    /**
     * Funció que comprova si el fitxer d'entrada rebut és de tipus ASMFile.
     * @return boolean que indica amb True que el fitxer és de tipus ASMFile, o amb False en cas contrari.
     */
    public boolean isInputASMFile() {
        return archiveModel.getInputFile() instanceof ASMFile;
    }

    /**
     * Funció que comprova si el fitxer d'entrada rebut és de tipus ObjectFile.
     * @return boolean que indica amb True que el fitxer és de tipus ObjectFile, o amb False en cas contrari.
     */
    public boolean isInputObjectFile() {
        return archiveModel.getInputFile() instanceof ObjectFile;
    }

    /**
     * Funció que comprova si el fitxer d'entrada rebut és de tipus CFile.
     * @return boolean que indica amb True que el fitxer és de tipus CFile, o amb False en cas contrari.
     */
    public boolean isInputCFile() {
        return archiveModel.getInputFile() instanceof CFile;
    }

    /**
     * Funció que s'encarrega de la lectura del fitxer d'entrada, independentment del seu tipus dins del marc de tipus soportats.
     * @return Archive amb la informació pertinent extreta en la lectura del fitxer.
     * @throws IOException Excepció originada a causa de no trobar el fitxer.
     */
    public Archive readInputFile() throws IOException {
        if (isInputCFile()) {
            CFile original = (CFile) archiveModel.getInputFile();
            CFile copied = new CFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            copied.readFile();
            return copied;
        }

        if (isInputASMFile()) {
            ASMFile original = (ASMFile) archiveModel.getInputFile();
            ASMFile copied = new ASMFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return copied;
        }

        if (isInputObjectFile()) {
            ObjectFile original = (ObjectFile) archiveModel.getInputFile();
            ObjectFile copied = new ObjectFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return copied;
        }

        if (isInputPEFile()) {
            PEFile original = (PEFile) archiveModel.getInputFile();
            Files.copy(original.getAbsoluteFile().toPath(), new File(RESOURCES_PATH + original.getName()).getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return new PEFile(new File(RESOURCES_PATH + original.getName()));
        }
        return null;
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer C.
     * @param cFile CFile amb la informació del fitxer a crear.
     */
    public void createCOutputFile(CFile cFile) {
        String name = getFinalPath(C_EXTENSION);
        try {
            Files.copy(cFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer ASM a partir d'un fitxer d'entrada C.
     * @param cFile CFile amb la informació del fitxer que permetrà la seva conversió a ASM.
     * @return ASMFile amb el fitxer resultant de la conversió del fitxer C.
     */
    public ASMFile createASMOutputFile(CFile cFile) {
        try {
            //gcc -S main.c
            ProcessBuilder builder = new ProcessBuilder(
                    "i686-w64-mingw32-gcc.exe", "-S", cFile.getAbsolutePath()).inheritIO();
            builder.redirectErrorStream(true);
            builder.directory(cFile.getParentFile());
            builder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        int extensionIndex = cFile.getAbsolutePath().lastIndexOf(".");

        return new ASMFile(new File(cFile.getAbsolutePath().substring(0, extensionIndex)
                + ASM_EXTENSION));
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer ASM.
     * @param asmFile ASMFile amb la informació del fitxer a crear.
     */
    public void createASMOutputFile(ASMFile asmFile) {
        String name = getFinalPath(ASM_EXTENSION);
        try {
            Files.copy(asmFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer binari de tipus objecte, a partir d'un fitxer d'entrada ASM.
     * @param asmFile ASMFile amb la informació del fitxer que permetrà la seva conversió a binari de tipus objecte.
     * @return ObjectFile amb el fitxer resultant de la conversió del fitxer ASM.
     */
    public ObjectFile createObjectOutputFile(ASMFile asmFile) {
        try {
            //gcc -c main.s
            ProcessBuilder builder = new ProcessBuilder(
                    "i686-w64-mingw32-gcc.exe", "-c", asmFile.getAbsolutePath()).inheritIO();
            builder.redirectErrorStream(true);
            builder.directory(asmFile.getParentFile());
            builder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        int extensionIndex = asmFile.getAbsolutePath().lastIndexOf(".");
        return new ObjectFile(new File(asmFile.getAbsolutePath().substring(0, extensionIndex)
                + OBJECT_EXTENSION));
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer binari objecte.
     * @param objectFile ObjectFile amb la informació del fitxer a crear.
     */
    public void createObjectOutputFile(ObjectFile objectFile) {
        String name = getFinalPath(OBJECT_EXTENSION);
        try {
            Files.copy(objectFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer binari de tipus executable, a partir d'un fitxer d'entrada binari objecte.
     * @param objectFile ObjectFile amb la informació del fitxer que permetrà la seva conversió a binari de tipus executable.
     * @return PEFile amb el fitxer resultant de la conversió del fitxer binari objecte.
     */
    public PEFile createExecutableOutputFile(ObjectFile objectFile) {
        try {
            //gcc main.o -o main.exe
            int extensionIndex = objectFile.getAbsolutePath().lastIndexOf(".");

            ProcessBuilder builder = new ProcessBuilder(
                    "i686-w64-mingw32-gcc.exe", objectFile.getAbsolutePath(), "-o", objectFile.getAbsolutePath().substring(0, extensionIndex) + EXE_EXTENSION).inheritIO();
            builder.redirectErrorStream(true);
            builder.directory(objectFile.getParentFile());
            builder.start().waitFor();

            extensionIndex = objectFile.getAbsolutePath().lastIndexOf(".");
            return new PEFile(new File(objectFile.getAbsolutePath().substring(0, extensionIndex)
                    + EXE_EXTENSION));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode que procedeix amb la creació d'un fitxer de sortida que compleixi amb el tipus de fitxer binari executable.
     * @param peFile PEFile amb la informació del fitxer a crear.
     */
    public void createExecutableOutputFile(PEFile peFile) {
        String name = getFinalPath(EXE_EXTENSION);
        try {
            Files.copy(peFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funció que processa la ruta del fitxer resultant, considerant així la pròpia ruta, el nom del fitxer, i la seva extensió.
     * @param extension String amb l'extensió del fitxer.
     * @return String amb la ruta del fitxer resultant.
     */
    private String getFinalPath(String extension){
        return ((Directory) archiveModel.getOutputDirectory()).getPath() + "\\" +
                FilenameUtils.removeExtension(archiveModel.getInputFile().getName()) +
                extension;
    }
}
