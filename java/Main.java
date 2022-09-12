import Controller.MainController;
import Model.MainModel;
import View.ProgramView;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        ProgramView programView;
        try {
            programView = new ProgramView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MainModel mainModel = new MainModel(System.getProperty("os.name"));
        new MainController(mainModel, programView);
        programView.setVisible(true);
    }
}
