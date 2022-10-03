package View;

import Controller.ViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import static Model.Settings.*;

/**
 * ProgramView és la classe on es renderitza i treballen tots els elements que es mostraran per pantalla, els quals l'usuari pot veure i interactuar.
 * Segueix l'arquitectura d'un patró MVC.
 * Aquest, vincula les interaccions de l'usuari amb la capa controlador (ViewController)
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class ProgramView extends JFrame {
    private JTextField text_FE, text_FS;
    private JButton button_FE, button_FS, button_CF, button_BN, button_BO, button_BE;
    private JCheckBox technique_EW, technique_SRS, technique_HE, technique_SBD, technique_PTDAW, technique_CRDP;
    private JFileChooser jFileChooser;

    /**
     * Únic constructor de la classe. Inicialitza i prepara el JFrame per a la vista a mostrar a l'usuari per pantalla.
     * @throws IOException Excepció originada a causa d'un error en la càrrega de la icona del programa.
     */
    public ProgramView() throws IOException {
        super(TITLE_VIEW);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(ImageIO.read(new File(ICON_PATH)));
        getRootPane().setBorder(new EmptyBorder(10,10,10,10));
        setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(createFilePanel(), BorderLayout.WEST);
        mainPanel.add(createTechniquesPanel(), BorderLayout.EAST);
        mainPanel.add(createOutputsGenerationPanel(), BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Mètode que s'encarrega de registrar els actuadors en format esdeveniments, entre la vista i el controlador especificats.
     * @param actionListener ActionListener que fa referència al controlador que tractarà la petició.
     */
    public void registerControllers(ActionListener actionListener) {
        button_FE.addActionListener(actionListener);
        button_FS.addActionListener(actionListener);
        button_CF.addActionListener(actionListener);
        button_BN.addActionListener(actionListener);
        button_BO.addActionListener(actionListener);
        button_BE.addActionListener(actionListener);

        technique_EW.addActionListener(actionListener);
        technique_SRS.addActionListener(actionListener);
        technique_HE.addActionListener(actionListener);
        technique_SBD.addActionListener(actionListener);
        technique_PTDAW.addActionListener(actionListener);
        technique_CRDP.addActionListener(actionListener);
    }

    /**
     * Creació de la vista JFileChooser, la qual permet la selecció de fitxers i directoris dins l'equip.
     * @return JFileChooser creat.
     */
    public JFileChooser createFileChooserView(){
        return new JFileChooser();
    }

    /**
     * Creació del panell vinculat a l'especificació del fitxer d'entrada i directori de sortida respectius.
     * @return JPanel pertinent a l'especificació de la respectiva entrada i sortida del programa.
     */
    private JPanel createFilePanel() {
        JPanel filePanel = new JPanel();
        text_FE = new JTextField(10);
        text_FE.setEditable(false);
        text_FS = new JTextField(10);
        text_FS.setEditable(false);
        button_FE = new JButton(BUTTON_INPUT);
        button_FE.setActionCommand(TAG_INPUT);
        button_FS = new JButton(BUTTON_OUTPUT);
        button_FS.setActionCommand(TAG_OUTPUT);
        button_FS.setEnabled(false);
        JPanel inputFilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel outputFilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        inputFilePanel.setBorder(BorderFactory.createTitledBorder(INPUT_FILE_TEXT));
        addComponentLeft(inputFilePanel,text_FE);
        text_FE.setEditable(false);
        addComponentLeft(inputFilePanel,button_FE);
        addComponentLeft(filePanel,inputFilePanel);

        outputFilePanel.setBorder(BorderFactory.createTitledBorder(OUTPUT_FILE_TEXT));
        addComponentLeft(outputFilePanel,text_FS);
        text_FS.setEditable(false);
        addComponentLeft(outputFilePanel,button_FS);
        addComponentLeft(filePanel,outputFilePanel);

        return filePanel;
    }

    /**
     * Creació del panell vinculat a la selecció de les possibles tècniques a aplicar en el programa.
     * @return JPanel pertinent a l'especificació de les possibles tècniques a aplicar.
     */
    private JPanel createTechniquesPanel() {
        JPanel techniquesPanel = new JPanel();
        techniquesPanel.setLayout(new BoxLayout(techniquesPanel, BoxLayout.Y_AXIS));
        techniquesPanel.setBorder(BorderFactory.createTitledBorder(TECHNIQUES));

        technique_EW = new JCheckBox(TECHNIQUE_EW);
        technique_EW.setEnabled(false);
        technique_EW.setActionCommand(TAG_TECHNIQUE_EW);
        addComponentLeft(techniquesPanel,technique_EW);
        technique_SRS = new JCheckBox(TECHNIQUE_SRS);
        technique_SRS.setEnabled(false);
        addComponentLeft(techniquesPanel,technique_SRS);
        technique_HE = new JCheckBox(TECHNIQUE_HE);
        technique_HE.setEnabled(false);
        addComponentLeft(techniquesPanel,technique_HE);
        technique_SBD = new JCheckBox(TECHNIQUE_SBD);
        technique_SBD.setEnabled(false);
        addComponentLeft(techniquesPanel,technique_SBD);
        technique_PTDAW = new JCheckBox(TECHNIQUE_PTDAW);
        technique_PTDAW.setEnabled(false);
        addComponentLeft(techniquesPanel,technique_PTDAW);
        technique_CRDP = new JCheckBox(TECHNIQUE_CRDP);
        technique_CRDP.setEnabled(false);
        addComponentLeft(techniquesPanel,technique_CRDP);

        return techniquesPanel;
    }

    /**
     * Creació del panell vinculat a la selecció de les possibles generacions del fitxer de sortida.
     * @return JPanel pertinent als diversos processos de generació del fitxer de sortida.
     */
    private JPanel createOutputsGenerationPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createTitledBorder(OUTPUT));

        button_CF = new JButton(OUTPUT_CF);
        button_CF.setEnabled(false);
        button_CF.setActionCommand(TAG_CF);
        buttonsPanel.add(button_CF);
        button_BN = new JButton(OUTPUT_BN);
        button_BN.setEnabled(false);
        button_BN.setActionCommand(TAG_BN);
        buttonsPanel.add(button_BN);
        button_BO = new JButton(OUTPUT_BO);
        button_BO.setEnabled(false);
        button_BO.setActionCommand(TAG_BO);
        buttonsPanel.add(button_BO);
        button_BE = new JButton(OUTPUT_BE);
        button_BE.setEnabled(false);
        button_BE.setActionCommand(TAG_BE);
        buttonsPanel.add(button_BE);

        return buttonsPanel;
    }

    /**
     * Funció que permet l'alineació a l'esquerra d'un component dins d'un JPanel, i posteriorment l'afegeix.
     * @param panel JPanel on afegir el component.
     * @param component JComponent que alinear a l'esquerra i posteriorment afegir-lo al JPanel especificat.
     */
    private void addComponentLeft(JPanel panel, JComponent component){
        component.setAlignmentX(LEFT_ALIGNMENT);
        component.setMaximumSize(component.getPreferredSize());
        panel.add(component);
    }

    /**
     * Mètode que s'encarrega de registrar la relació entre els elements de la vista JCheckBox i el ItemListener especificat, resultant d'un controlador.
     * @param itemListener ItemListener pertinent al controlador especificat.
     */
    public void addItemListener(ItemListener itemListener){
        technique_EW.addItemListener(itemListener);
        technique_SRS.addItemListener(itemListener);
        technique_HE.addItemListener(itemListener);
        technique_SBD.addItemListener(itemListener);
        technique_PTDAW.addItemListener(itemListener);
        technique_CRDP.addItemListener(itemListener);
    }

    /**
     * Mètode que s'encarrega de crear un ItemListener a partir del controlador especificat.
     * @param viewController ViewController pertinent al controlador especificat
     * @return ItemListener creat.
     */
    public ItemListener createItemListener(ViewController viewController){
        return e -> {
            boolean selected = e.getStateChange() == ItemEvent.SELECTED;
            if(e.getSource() == technique_EW) viewController.itemChanged(TAG_TECHNIQUE_EW,selected);
            if(e.getSource() == technique_SRS) viewController.itemChanged(TAG_TECHNIQUE_SRS,selected);
            if(e.getSource() == technique_HE) viewController.itemChanged(TAG_TECHNIQUE_HE,selected);
            if(e.getSource() == technique_SBD) viewController.itemChanged(TAG_TECHNIQUE_SBD,selected);
            if(e.getSource() == technique_PTDAW) viewController.itemChanged(TAG_TECHNIQUE_PTDAW,selected);
            if(e.getSource() == technique_CRDP) viewController.itemChanged(TAG_TECHNIQUE_CRDP,selected);
        };
    }

    /**
     * Setter del text de la variable JTextField text_FE.
     * @param text String amb el text a afegir a la variable text_FE.
     */
    public void setInputText(String text){
        text_FE.setText(text);
    }

    /**
     * Setter del text de la variable JTextField text_FS.
     * @param text String amb el text a afegir a la variable text_FS.
     */
    public void setOutputText(String text){
        text_FS.setText(text);
    }

    /**
     * Creació d'un diàleg d'error que mostra el títol i missatges especificats.
     * @param title String del títol a mostrar en el missatge d'error.
     * @param message String del text del missatge a mostrar en el missatge d'error.
     */
    public void showErrorMessage(String title, String message){
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creació d'un diàleg d'informació que mostra el títol i missatges especificats.
     * @param title String del títol a mostrar en el missatge d'informació.
     * @param message String del text del missatge a mostrar en el missatge d'informació.
     */
    public void showInfoMessage(String title, String message){
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica Encryption Wrappers.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_EW(boolean enable){
        technique_EW.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica Stripping Redundant Symbols.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_SRS(boolean enable){
        technique_SRS.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica Header Entrypoint.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_HE(boolean enable){
        technique_HE.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica Software Breakpoint Detection.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_SBD(boolean enable){
        technique_SBD.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica PT_DenyAttachWorked.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_PTDAW(boolean enable){
        technique_PTDAW.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JCheckBox pertinent a la tècnica CheckRemoteDebuggerPresent.
     * @param enable boolean amb l'estat de disponibilitat de la tècnica pertinent.
     */
    public void setTechnique_CRDP(boolean enable){
        technique_CRDP.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JButton pertinent a la generació del fitxer de sortida en codi font.
     * @param enable boolean amb l'estat de la disponibilitat del botó pertinent.
     */
    public void setButton_CF(boolean enable){
        button_CF.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JButton pertinent a la generació del fitxer de sortida en codi de baix nivell.
     * @param enable boolean amb l'estat de la disponibilitat del botó pertinent.
     */
    public void setButton_BN(boolean enable){
        button_BN.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JButton pertinent a la generació del fitxer de sortida en binari de tipus objecte.
     * @param enable boolean amb l'estat de la disponibilitat del botó pertinent.
     */
    public void setButton_BO(boolean enable){
        button_BO.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JButton pertinent a la generació del fitxer de sortida en binari de tipus executable.
     * @param enable boolean amb l'estat de la disponibilitat del botó pertinent.
     */
    public void setButton_BE(boolean enable){
        button_BE.setEnabled(enable);
    }

    /**
     * Setter de la disponibilitat del JButton pertinent a la selecció del directori de sortida.
     * @param enable boolean amb l'estat de la disponibilitat del botó pertinent.
     */
    public void setButton_FS(boolean enable){
        button_FS.setEnabled(enable);
    }

    /**
     * Funció que determina si el camp de text específic pertinent al JTextField de la sortida del programa no està buit.
     * @return boolean que indica true en cas que dit JTextField no estigui buit, i fals en cas contrari.
     */
    public boolean hasText_FS(){
        return text_FS.getText().compareTo("") != 0;
    }

    /**
     * Getter de la variable JFileChooser per a la selecció de fitxers i directoris.
     * @return JFileChooser en qüestió.
     */
    public JFileChooser getJFileChooser() {
        return jFileChooser;
    }

    /**
     * Setter de la variable JFileChooser per a la selecció de fitxers i directoris.
     * @param jFileChooser JFileChooser en qüestió
     */
    public void setJFileChooser(JFileChooser jFileChooser){
        this.jFileChooser = jFileChooser;
    }
}