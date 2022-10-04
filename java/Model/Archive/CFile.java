package Model.Archive;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ASMFile és una classe que estén de File i implementa la interfície Archive.
 * Permet el tractament i diferenciació dels arxius de tipus codi font de llenguatge C.
 *
 * @author Jaume Campeny
 * @version 1.0
 * @since 17
 */
public class CFile extends File implements Archive {
    private final ArrayList<Function> functions;
    private final ArrayList<Include> includes;

    /**
     * Constructor de la classe.
     * @param file File del fitxer CFile a instanciar.
     */
    public CFile(File file) {
        super(file.getPath());
        functions = new ArrayList<>();
        includes = new ArrayList<>();
    }

    /**
     * Mètode que efectua la lectura i posterior càrrega d'informació en el fitxer.
     * @throws IOException Excepció originada a causa de no trobar el fitxer.
     */
    public void readFile() throws IOException {
        int line = 0;
        Scanner scanner = new Scanner(this);
        while (scanner.hasNext()) {
            treatLine(scanner.nextLine(), ++line);
        }
        scanner.close();
    }

    /**
     * Mètode que efectua una modificació en el fitxer en qüestió, afegint el text indicat en el paràmetre text, en la línia line.
     * @param text String amb el text a afegir.
     * @param line int de la línia on afegir el text.
     */
    public void addTextToFile(String text,int line){
        int actualLine = 0;
        try {
            File auxiliar = new File(this.getAbsolutePath() + "tmp");
            FileUtils.copyFile(this, auxiliar);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this,false)));
            Scanner scanner = new Scanner(auxiliar);
            while(scanner.hasNext()){
                if(line == ++actualLine){
                    writer.println(text);
                    //Actualitzar els índexs de les línies en els imports i declaració de funcions.
                    for(Function function : functions){
                        if(function.getDeclaredLine() >= actualLine){
                            function.setDeclaredLine(function.getDeclaredLine() + (int)text.lines().count());
                        }
                    }
                    for(Include include : includes){
                        if(include.getDeclaredLine() >= actualLine){
                            include.setDeclaredLine(include.getDeclaredLine() + (int)text.lines().count());
                        }
                    }
                }
                writer.println(scanner.nextLine());
            }
            scanner.close();
            writer.close();
            FileUtils.delete(auxiliar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mètode encarregat d'interpretar la línia de text del paràmetre line i extreure informació important com declaracions de includes i funcions, així com la línia en la qual apareixen (lineNumber).
     * @param line String amb el text de la línia a interpretar
     * @param lineNumber int número de la línia line dins del fitxer.
     */
    private void treatLine(String line, int lineNumber) {
        if (line.compareTo("") == 0) return;
        Pattern includePattern = Pattern.compile(Include.INCLUDE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher includeMatcher = includePattern.matcher(line);
        if (includeMatcher.find()) includes.add(new Include(includeMatcher.group(1), lineNumber));

        Pattern functionPattern = Pattern.compile(Function.FUNCTION_DECLARATION_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher functionMatcher = functionPattern.matcher(line);
        if(functionMatcher.find()) functions.add(new Function(functionMatcher.group(1), lineNumber));
    }

    /**
     * Getter de la mida total de declaracions de funcions trobades.
     * @return int amb el total de declaracions de funcions trobades en el fitxer.
     */
    public int getFunctionsSize(){
        return functions.size();
    }

    /**
     * Getter de la funció en la posició índex.
     * @param index int amb l'índex de la posició de la funció a consultar.
     * @return Function trobada en l'índex especificat.
     */
    public Function getFunction(int index){
        return functions.get(index);
    }

    /**
     * Mètode que permet l'addició d'una nova funció en memòria, considerant el seu nom amb el paràmetre functionName, i la línia on està declarada, amb el paràmetre declaredLine.
     * @param functionName String del nom de la funció a afegir.
     * @param declaredLine int de la línia on la funció està declarada en el fitxer.
     */
    public void addFunction(String functionName, int declaredLine){
        functions.add(new Function(functionName, declaredLine));
    }

    /**
     * Mètode que permet l'addició d'un nou include en memòria, considerant el seu nom amb el paràmetre includeName, i la línia on està declarat, amb el paràmetre declaredLine.
     * @param includeName String del nom del include a afegir.
     * @param declaredLine int de la línia on l'include està declarat en el fitxer.
     */
    public void addInclude(String includeName, int declaredLine){
        includes.add(new Include(includeName, declaredLine));
    }

    /**
     * Funció que confirma l'existència de l'include amb nom includeName.
     * @param includeName String amb el nom de l'include a consultar.
     * @return boolean que indica l'existència de l'include amb un True, i amb un False el cas contrari.
     */
    public boolean existsInclude(String includeName){
        for(Include include : includes){
            if(include.getIncludeName().compareTo(includeName) == 0) return true;
        }
        return false;
    }

    /**
     * Getter de la línia de la declaració de la funció amb nom functionName.
     * @param functionName String amb el nom de la funció en qüestió.
     * @return int amb la línia on està declarada la funció, o -1 en cas contrari.
     */
    public int getFunctionLine(String functionName){
        for(Function function : functions){
            if(function.getFunctionName().compareTo(functionName) == 0) return function.getDeclaredLine();
        }
        return -1;
    }

    /**
     * Funció que retorna la línia de l'últim include declarat en el fitxer.
     * @return int de la línia pertinent a l'últim include.
     */
    public int getLastIncludeLine(){
        int lastInclude = 1;
        for(Include include : includes){
            if(include.getDeclaredLine() > lastInclude) lastInclude = include.getDeclaredLine();
        }
        return lastInclude;
    }

    /**
     * Classe Function. Permet el tractament de declaracions de funcions trobades en fitxers .C.
     */
    public static class Function {
        public static final String FUNCTION_DECLARATION_REGEX = "^\\S* *(.*)\\(.*\\) *\\{";
        private final String functionName;
        private int declaredLine;

        /**
         * Constructor de la classe. Sobreescriu el nom de la funció i la línia on es troba, de la instància creada.
         * @param functionName String amb el nom de la funció.
         * @param declaredLine int amb la línia on es troba dins del fitxer.
         */
        public Function(String functionName, int declaredLine) {
            this.functionName = functionName;
            this.declaredLine = declaredLine;
        }

        /**
         * Getter del nom de la funció.
         * @return String amb el nom de la funció.
         */
        public String getFunctionName() {
            return functionName;
        }

        /**
         * Getter de la línia en la qual es declara la funció.
         * @return int amb la línia en la qual es declara la funció.
         */
        public int getDeclaredLine() {
            return declaredLine;
        }

        /**
         * Setter de la línia en la qual es declara la funció.
         * @param declaredLine int amb la línia en la qual es declara la funció.
         */
        public void setDeclaredLine(int declaredLine) {
            this.declaredLine = declaredLine;
        }
    }

    /**
     * Classe Include. Permet el tractament de declaracions de includes trobats en fitxers .C.
     */
    public static class Include {
        public static final String INCLUDE_REGEX = "^#include *[\"<](.*)[\">]";
        private final String includeName;
        private int declaredLine;

        /**
         * Constructor de la classe. Sobreescriu el nom de la llibreria especificat en el include i la línia on es troba, de la instància creada.
         * @param includeName String amb el nom de la llibreria especificada en el include.
         * @param declaredLine int amb la línia on es troba dins del fitxer.
         */
        public Include(String includeName, int declaredLine) {
            this.includeName = includeName;
            this.declaredLine = declaredLine;
        }

        /**
         * Getter del nom de la llibreria especificat en el include.
         * @return String amb el nom de la llibreria especificat en el include.
         */
        public String getIncludeName() {
            return includeName;
        }

        /**
         * Getter de la línia en la qual es declara el include.
         * @return int de la línia on el include és declarat.
         */
        public int getDeclaredLine() {
            return declaredLine;
        }

        /**
         * Setter de la línia en la qual es declara el include
         * @param declaredLine int de la línia on el include és declarat.
         */
        public void setDeclaredLine(int declaredLine) {
            this.declaredLine = declaredLine;
        }
    }
}
