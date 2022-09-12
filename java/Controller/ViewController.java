package Controller;

import View.ProgramView;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static Model.Settings.*;

public class ViewController implements ActionListener {
    private final ProgramView programView;
    private final MainController mainController;

    public ViewController(MainController mainController, ProgramView programView) {
        this.mainController = mainController;
        this.programView = programView;
        programView.registerControllers(this);
        programView.addItemListener(programView.createItemListener(this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String tag = e.getActionCommand();
        if (tag.contains(TAG_INPUT) || tag.contains(TAG_OUTPUT)) mainController.actionPerformedFile(programView, tag);
        if (tag.contains(TAG_CF) || tag.contains(TAG_BN) || tag.contains(TAG_BO) || tag.contains(TAG_BE))
            mainController.actionPerformedGenerate(tag);
    }

    public void itemChanged(String flag, boolean selected){
        mainController.actionPerformedTechnique(flag, selected);
    }

    public void disableAllTechniques() {
        programView.setTechnique_MBHS(false);
        programView.setTechnique_SRS(false);
        programView.setTechnique_HE(false);
        programView.setTechnique_SBD(false);
        programView.setTechnique_PTDAW(false);
        programView.setTechnique_CRDP(false);
    }

    public void enableTechnique_MBHS() {
        programView.setTechnique_MBHS(true);
    }

    public void enableTechnique_SRS() {
        programView.setTechnique_SRS(true);
    }

    public void enableTechnique_HE() {
        programView.setTechnique_HE(true);
    }

    public void enableTechnique_SBD() {
        programView.setTechnique_SBD(true);
    }

    public void enableTechnique_PTDAW() {
        programView.setTechnique_PTDAW(true);
    }

    public void enableTechnique_CRDP() {
        programView.setTechnique_CRDP(true);
    }

    public void enableAllGenerations(){
        programView.setButton_CF(true);
        programView.setButton_BN(true);
        programView.setButton_BO(true);
        programView.setButton_BE(true);
    }

    public void disableButton_CF(){
        programView.setButton_CF(false);
    }

    public void disableButton_BN(){
        programView.setButton_BN(false);
    }

    public void disableButton_BO(){
        programView.setButton_BO(false);
    }

    public void disableButton_BE(){
        programView.setButton_BE(false);
    }

    public void enableOutputDialog(){
        programView.setButton_FS(true);
    }

    public boolean isOutputSelected(){
        return programView.hasText_FS();
    }

    public void showInfoMessage(String title, String message){
        programView.showInfoMessage(title,message);
    }

    public void showErrorMessage(String title, String message){
        programView.showErrorMessage(title,message);
    }

    public int createFileDialog(int option, FileNameExtensionFilter fileNameExtensionFilter){
        programView.setJFileChooser(programView.createFileChooserView());
        programView.getJFileChooser().setFileSelectionMode(option);
        programView.getJFileChooser().setFileFilter(fileNameExtensionFilter);
        return programView.getJFileChooser().showOpenDialog(programView);
    }

    public File getSelectedFile(){
        return programView.getJFileChooser().getSelectedFile();
    }

}
