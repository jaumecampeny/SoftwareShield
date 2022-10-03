package Controller;

import Model.MainModel;
import View.ProgramView;

import static Model.Settings.*;

/**
 * MainController és la classe on s'implementa la inicialització de tots els controladors del programa, d'afegit a la relació d'aquests amb els respectius models que necessitaran.
 * Seguint un patró MVC, en aquesta classe s'entrellaça les accions de l'usuari que es reben a través de ProgramView, amb la lògica de l'aplicació pertinent als altres controladors i respectius models.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class MainController {
    private final FileController fileController;
    private final ViewController viewController;
    private final TechniqueController techniqueController;
    private final GenerateController generateController;

    /**
     * Únic controlador de la classe. Inicialitza la resta de controladors (FileController, ViewController, TehcniqueController i GenerateController), i els relaciona amb els respectius models i el mòdul vista pertinents.
     * @param mainModel MainModel com a classe principal del mòdul Model. Utilitzada per a relacionar els respectius models amb les inicialitzacions dels controladors.
     * @param programView ProgramView com a mòdul Vista. Utilitzada per a relacionar el controlador de la vista amb el mateix mòdul vista.
     */
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

    /**
     * Mètode que és cridat des de ViewController, tractant les accions de l'usuari que es portin a terme en el panell de tècniques.
     * Segons el boolean introduït, es determina si la tècnica està o no seleccionada, actualitzant la mateixa en conseqüència.
     * S'encarrega de la lògica pertinent a la selecció de les tècniques, actualitzant les respectives seleccions, i alhora cridar l'actualització de les possibilitats en la generació dels fitxers.
     * @param flag String amb l'identificador de la tècnica a actualitzar.
     * @param selected boolean amb l'estat de la tècnica especificada, indicant si l'usuari l'ha seleccionat o desseleccionat.
     */
    public void actionPerformedTechnique(String flag, boolean selected) {
        techniqueController.setTechniqueSelected(flag, selected);
        generateController.setGenerateOptions();
    }


    /**
     * Mètode que és cridat des de ViewController, tractant les accions de l'usuari que es portin a terme en el panell de generació del fitxer resultant.
     * Segons el flag dels paràmetres, es determina quina generació ha estat escollida per l'usuari, i s'envia l'ordre al controlador corresponent del procés de generació (GenerateController).
     * @param flag String amb l'identificador del tipus de generació triat per l'usuari.
     */
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
