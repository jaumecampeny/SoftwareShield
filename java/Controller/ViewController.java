package Controller;

import View.ProgramView;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static Model.Settings.*;

/**
 * ViewController és la classe que implementa el control directe amb la vista del programa, seguint amb l'arquitectura d'un patró MVC.
 * Aquest és l'encarregat de modificar la vista segons convingui i dictin les accions i esdeveniments generats per l'usuari.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class ViewController implements ActionListener {
    private final ProgramView programView;
    private final MainController mainController;

    /**
     * Únic constructor de classe. Permet la creació de la instància ViewController, mitjançant l'enregistrament de les instàncies rebudes pertinent al mòdul de vista i controlador.
     * @param mainController MainController que permetrà l'enviament d'accions a la resta de controladors mitjançant el control de flux que permet MainController.
     * @param programView ProgramView a vincular.
     */
    public ViewController(MainController mainController, ProgramView programView) {
        this.mainController = mainController;
        this.programView = programView;
        programView.registerControllers(this);
        programView.addItemListener(programView.createItemListener(this));
    }

    /**
     * Mètode que s'encarrega de l'escolta dels esdeveniments que efectua l'usuari mitjançant el mòdul Vista, i processa la petició contra MainController,
     * qui redirigirà la petició segons convingui.
     * @param e ActionEvent que inclou la informació de l'element amb el qual s'ha interactuat.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String tag = e.getActionCommand();
        if (tag.contains(TAG_INPUT) || tag.contains(TAG_OUTPUT)) mainController.actionPerformedFile(tag);
        if (tag.contains(TAG_CF) || tag.contains(TAG_BN) || tag.contains(TAG_BO) || tag.contains(TAG_BE))
            mainController.actionPerformedGenerate(tag);
    }

    /**
     * Mètode que efectua la petició del canvi en l'estat de selecció d'un element específic indicant pel paràmetre flag.
     * Processa la petició contra MainController, qui redirigirà la petició segons convingui.
     * @param flag String amb l'identificador de l'element a modificar l'estat de selecció.
     * @param selected boolean indicant l'estat de selecció de dit element. True si està seleccionat, o False en cas contrari.
     */
    public void itemChanged(String flag, boolean selected){
        mainController.actionPerformedTechnique(flag, selected);
    }

    /**
     * Mètode que inhabilita totes les tècniques simultàniament.
     */
    public void disableAllTechniques() {
        programView.setTechnique_EW(false);
        programView.setTechnique_SRS(false);
        programView.setTechnique_HE(false);
        programView.setTechnique_SBD(false);
        programView.setTechnique_PTDAW(false);
        programView.setTechnique_CRDP(false);
    }

    /**
     * Setter que habilita la tècnica Encryption Wrappers.
     */
    public void enableTechnique_EW() {
        programView.setTechnique_EW(true);
    }

    /**
     * Setter que habilita la tècnica Stripping Redundant Symbols.
     */
    public void enableTechnique_SRS() {
        programView.setTechnique_SRS(true);
    }

    /**
     * Setter que habilita la tècnica Header Entrypoint.
     */
    public void enableTechnique_HE() {
        programView.setTechnique_HE(true);
    }

    /**
     * Setter que habilita la tècnica Software Breakpoint Detection.
     */
    public void enableTechnique_SBD() {
        programView.setTechnique_SBD(true);
    }

    /**
     * Setter que habilita la tècnica PT_DenyAttachWorked.
     */
    public void enableTechnique_PTDAW() {
        programView.setTechnique_PTDAW(true);
    }

    /**
     * Setter que habilita la tècnica CheckREmoteDebuggerPresent.
     */
    public void enableTechnique_CRDP() {
        programView.setTechnique_CRDP(true);
    }

    /**
     * Mètode que habilita totes les possibilitats de generació simultàniament.
     */
    public void enableAllGenerations(){
        programView.setButton_CF(true);
        programView.setButton_BN(true);
        programView.setButton_BO(true);
        programView.setButton_BE(true);
    }

    /**
     * Setter que inhabilita la generació de fitxers de tipus codi font.
     */
    public void disableButton_CF(){
        programView.setButton_CF(false);
    }

    /**
     * Setter que inhabilita la generació de fitxers de tipus codi de baix nivell.
     */
    public void disableButton_BN(){
        programView.setButton_BN(false);
    }

    /**
     * Setter que inhabilita la generació de fitxers de tipus binari de tipus objecte.
     */
    public void disableButton_BO(){
        programView.setButton_BO(false);
    }

    /**
     * Setter que inhabilita la generació de fitxers de tipus binari executables.
     */
    public void disableButton_BE(){
        programView.setButton_BE(false);
    }

    /**
     * Setter que habilita la part pertinent a la sortida del programa
     */
    public void enableOutputDialog(){
        programView.setButton_FS(true);
    }

    /**
     * Funció que comprova si s'ha especificat una ruta de sortida per als fitxers generats.
     * @return boolean que indica amb True que s'ha especificat la ruta de sortida, i amb False en cas contrari.
     */
    public boolean isOutputSelected(){
        return programView.hasText_FS();
    }

    /**
     * Mètode que genera un missatge d'informació amb el títol i cos del missatge especificats.
     * @param title String amb el títol del missatge.
     * @param message String amb el cos del missatge.
     */
    public void showInfoMessage(String title, String message){
        programView.showInfoMessage(title,message);
    }

    /**
     * Mètode que genera un missatge d'error amb el títol i cos del missatge especificats.
     * @param title String amb el títol del missatge.
     * @param message String amb el cos del missatge.
     */
    public void showErrorMessage(String title, String message){
        programView.showErrorMessage(title,message);
    }

    /**
     * Funció que s'encarrega tant de cridar la interfície de selecció de fitxers i directoris a partir de les opcions i filtres rebuts per paràmetres.
     * @param option int amb l'opció de funcionament de la interfície de selecció, la qual considera tant fitxers com directoris, o només un dels dos.
     * @param fileNameExtensionFilter FileNameExtensionFilter que especifica els tipus de fitxers quant a extensions que es visualitzaran des de la interfície.
     * @return int que indica la correctesa de l'operació, utilitzant constants CANCEL_OPTION, APPROVE_OPTION i ERROR_OPTION de la classe JFileChooser.
     */
    public int createFileDialog(int option, FileNameExtensionFilter fileNameExtensionFilter){
        programView.setJFileChooser(programView.createFileChooserView());
        programView.getJFileChooser().setFileSelectionMode(option);
        if(fileNameExtensionFilter != null) programView.getJFileChooser().setFileFilter(fileNameExtensionFilter);
        return programView.getJFileChooser().showOpenDialog(programView);
    }

    /**
     * Getter del fitxer seleccionat com a entrada del programa.
     * @return File del fitxer seleccionat.
     */
    public File getSelectedFile(){
        return programView.getJFileChooser().getSelectedFile();
    }

    /**
     * Setter del text pertinent a la selecció de l'entrada del programa
     * @param text String del text a mostrar.
     */
    public void setInputText(String text){
        programView.setInputText(text);
    }

    /**
     * Setter del text pertinent a la selecció de la sortida del programa
     * @param text String del text a mostrar.
     */
    public void setOutputText(String text){
        programView.setOutputText(text);
    }

}
