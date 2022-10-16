import Controller.MainController;
import Model.MainModel;
import View.ProgramView;

import java.io.*;
import java.net.URL;

import static Model.Settings.ICON_NAME;

/**
 * Classe main. Fil principal d'execució.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class Main {

    /**
     * Mètode static void main. On s'inicia el programa.
     * @param args String[] amb els possibles paràmetres a introduir. No utilitzats per aquest programa.
     */
    public static void main(String[] args) {

        /*
            S'ha fet ús del patró de disseny MVC. Aquest patró permet el desenvolupament d'interfícies d'usuari dividint el programa en 3 parts interconnectades:
                · Vista: També coneguda com a interfície de l'usuari. Presenta el model (informació i lògica de l'aplicació) en un format adequat per a interpretar (en aquest cas en format Swing).
                    D'afegit, també està pendent de les actualitzacions que realitza l'usuari envers la vista, per a notificar degudament al controlador mitjançant esdeveniments.
                · Model: Representació de la informació amb la qual el sistema opera. Es gestionen tots els accessos a la informació, on es fan ús de diversos mètodes segons convingui, d'entre els
                    que destaquen la transparència d'accessos i manipulacions de fitxers (tant d'entrada com de sortida), les tècniques, i finalment paràmetres generals del programa.
                · Controlador: És l'encarregat de lligar el mòdul model amb la vista. La vista notifica al Controlador davant les accions que duu a terme l'usuari contra la interfície.
                    El controlador escolta aquestes notificacions i aplica la lògica pertinent al tractament d'aquestes, executant la lògica de l'aplicació que es troba en el model o en la mateixa vista.

            S'ha aplicat el patró MVC per a permetre una abstracció i repartició lògica de tasques entre les diferents classes del programa. Alliberant la càrrega dels mòduls principals i facilitant l'enteniment
            del programa gràcies a la distribució de mòduls lògics. A més a més d'un patró de disseny, també se'l considera un patró d'arquitectura, ja que afecta pròpiament a tot l'abast del programa i el plantejament d'aquest.

            Pel que fa al codi, el patró MVC engloba la classe ProgramView com a mòdul de Vista; les classes ArchiveModel, Technique Model, les derivades del mòdul Archive, les Settings del programa i finalment el MainModel
            com a integrants del mòdul del model; i finalment 5 controladors per a encarregar-se de tota la lògica pertinent a interaccions entre vista i model, d'on destaca ViewController com a controlador de la vista
            i actuador davant les accions de l'usuari contra la interfície (fent ús de MainController per a parlar amb el mòdul de model), FileController com a controlador dels fitxers tant d'entrada com de sortida,
            TechniqueController com a controlador davant a la lògica de possibles seleccions de tècniques i l'aplicació de les mateixes mitjançant el controlador FileController per a editar els fitxers degudament.
            Seguidament, GenerateController com a controlador principal a l'hora de dictaminar la lògica dels processos a realitzar en conseqüència a la petició de generació de fitxer per part de l'usuari, on entra l'actuació
            de tots els controladors en els dits processos. Finalment, el controlador MainController el qual permet la creació i vinculació correcta de tots els controladors amb els respectius models, d'afegit a la relació entre les peticions
            de l'usuari amb els altres controladors del programa.
         */
        ProgramView programView;

        try {
            programView = new ProgramView(Main.class.getClassLoader().getResourceAsStream(ICON_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MainModel mainModel = new MainModel(System.getProperty("os.name"), Main.class.getClassLoader().getResource("EW"));
        new MainController(mainModel, programView);
        programView.setVisible(true);
    }
}
