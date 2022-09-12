package Controller;

import Model.Archive.*;
import Model.FileModel;
import View.ProgramView;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

import static Model.FileModel.*;
import static Model.Settings.*;

public class FileController {
    private final FileModel fileModel;
    private final ViewController viewController;

    public FileController(FileModel fileModel, ViewController viewController) {
        this.fileModel = fileModel;
        this.viewController = viewController;
    }

    public void actionPerformedInputFile(ProgramView programView) {
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
                programView.showErrorMessage(ERROR_WRONG_EXTENSION_TITLE, ERROR_WRONG_EXTENSION_MESSAGE);
                return;
            }

            try {
                fileModel.loadFile(fileName, extension.get(), FileModel.INPUT_FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            programView.setInputText(fileName.getName());
            programView.showInfoMessage(INFO_CORRECT_FILE_TITLE, INFO_CORRECT_FILE_MESSAGE);
        }
    }

    public void actionPerformedOutputFile(ProgramView programView) {
        JFileChooser jFileChooser = programView.createFileChooserView();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = jFileChooser.showOpenDialog(programView);
        if (result == JFileChooser.APPROVE_OPTION) {
            File directory = jFileChooser.getSelectedFile();
            try {
                fileModel.loadFile(directory, null, FileModel.OUTPUT_FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            programView.setOutputText(directory.getName());
        }
    }

    public boolean isInputPEFile() {
        return fileModel.getInputFile() instanceof PEFile;
    }

    public boolean isInputASMFile() {
        return fileModel.getInputFile() instanceof ASMFile;
    }

    public boolean isInputObjectFile() {
        return fileModel.getInputFile() instanceof ObjectFile;
    }

    public boolean isInputCFile() {
        return fileModel.getInputFile() instanceof CFile;
    }

    public Archive readInputFile() throws IOException {
        if (isInputCFile()) {
            CFile original = (CFile) fileModel.getInputFile();
            CFile copied = new CFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            copied.readFile();
            return copied;
        }

        if (isInputASMFile()) {
            ASMFile original = (ASMFile) fileModel.getInputFile();
            ASMFile copied = new ASMFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return copied;
        }

        if (isInputObjectFile()) {
            ObjectFile original = (ObjectFile) fileModel.getInputFile();
            ObjectFile copied = new ObjectFile(new File(RESOURCES_PATH + original.getName()));
            Files.copy(original.getAbsoluteFile().toPath(), copied.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return copied;
        }

        if (isInputPEFile()) {
            PEFile original = (PEFile) fileModel.getInputFile();
            Files.copy(original.getAbsoluteFile().toPath(), new File(RESOURCES_PATH + original.getName()).getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            return new PEFile(new File(RESOURCES_PATH + original.getName()));
        }
        return null;
    }

    public void createCOutputFile(CFile cFile) {
        String name = ((Archive.Directory) fileModel.getOutputDirectory()).getPath() + "\\" +
                FilenameUtils.removeExtension(fileModel.getInputFile().getName()) +
                C_EXTENSION;
        try {
            Files.copy(cFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void createASMOutputFile(ASMFile asmFile) {
        String name = ((Archive.Directory) fileModel.getOutputDirectory()).getPath() + "\\" +
                FilenameUtils.removeExtension(fileModel.getInputFile().getName()) +
                ASM_EXTENSION;
        try {
            Files.copy(asmFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void createObjectOutputFile(ObjectFile objectFile) {
        String name = ((Archive.Directory) fileModel.getOutputDirectory()).getPath() + "\\" +
                FilenameUtils.removeExtension(fileModel.getInputFile().getName()) +
                OBJECT_EXTENSION;
        try {
            Files.copy(objectFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void createExecutableOutputFile(PEFile peFile) {
        String name = ((Archive.Directory) fileModel.getOutputDirectory()).getPath() + "\\" +
                FilenameUtils.removeExtension(fileModel.getInputFile().getName()) +
                EXE_EXTENSION;
        try {
            Files.copy(peFile.getAbsoluteFile().toPath(), new File(name).getAbsoluteFile().toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
