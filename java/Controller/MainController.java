package Controller;

import Model.MainModel;
import View.ProgramView;

import static Model.Settings.*;

public class MainController {
    private final FileController fileController;
    private final ViewController viewController;
    private final TechniqueController techniqueController;
    private final GenerateController generateController;

    public MainController(MainModel mainModel, ProgramView programView) {
        this.viewController = new ViewController(this, programView);
        this.fileController = new FileController(mainModel.getFileModel(), viewController);
        this.techniqueController = new TechniqueController(fileController, viewController, mainModel.getTechniqueModel(), mainModel.getOS());
        this.generateController = new GenerateController(techniqueController, viewController, fileController);
    }

    public void actionPerformedFile(ProgramView programView, String flag) {
        switch (flag) {
            case TAG_INPUT -> {
                fileController.actionPerformedInputFile(programView);
                viewController.enableOutputDialog();
                if (viewController.isOutputSelected()) techniqueController.setTechniqueOptions();
            }
            case TAG_OUTPUT -> {
                fileController.actionPerformedOutputFile(programView);
                techniqueController.setTechniqueOptions();
            }
        }
    }

    public void actionPerformedTechnique(String flag, boolean selected) {
        techniqueController.setTechniqueSelected(flag, selected);
        generateController.setGenerateOptions();
    }


    public void actionPerformedGenerate(String flag) {
        switch (flag) {
            case TAG_CF -> generateController.generate_CF();
            case TAG_BN -> generateController.generate_BN();
            case TAG_BO -> generateController.generate_BO();
            case TAG_BE -> generateController.generate_BE();
        }
        viewController.showInfoMessage("Fitxer creat","El fitxer s'ha creat correctament.");
    }
}
