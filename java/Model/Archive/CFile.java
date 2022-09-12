package Model.Archive;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFile extends File implements Archive {
    private final ArrayList<Function> functions;
    private final ArrayList<Include> includes;

    public CFile(File file) {
        super(file.getPath());
        functions = new ArrayList<>();
        includes = new ArrayList<>();
    }

    public void readFile() throws IOException {
        int line = 0;
        Scanner scanner = new Scanner(this);
        while (scanner.hasNext()) {
            treatLine(scanner.nextLine(), ++line);
        }
        scanner.close();
    }

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

    private void treatLine(String line, int lineNumber) {
        if (line.compareTo("") == 0) return;
        Pattern includePattern = Pattern.compile(Include.INCLUDE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher includeMatcher = includePattern.matcher(line);
        if (includeMatcher.find()) includes.add(new Include(includeMatcher.group(1), lineNumber));

        Pattern functionPattern = Pattern.compile(Function.FUNCTION_DECLARATION_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher functionMatcher = functionPattern.matcher(line);
        if(functionMatcher.find()) functions.add(new Function(functionMatcher.group(1), lineNumber));
    }

    public int getFunctionsSize(){
        return functions.size();
    }

    public Function getFunction(int index){
        return functions.get(index);
    }

    public void addFunction(String functionName, int declaredLine){
        functions.add(new Function(functionName, declaredLine));
    }

    public void addInclude(String includeName, int declaredLine){
        includes.add(new Include(includeName, declaredLine));
    }

    public boolean existsInclude(String includeName){
        for(Include include : includes){
            if(include.getIncludeName().compareTo(includeName) == 0) return true;
        }
        return false;
    }

    public int getFunctionLine(String functionName){
        for(Function function : functions){
            if(function.getFunctionName().compareTo(functionName) == 0) return function.getDeclaredLine();
        }
        return -1;
    }

    public int getLastIncludeLine(){
        int lastInclude = 1;
        for(Include include : includes){
            if(include.getDeclaredLine() > lastInclude) lastInclude = include.getDeclaredLine();
        }
        return lastInclude;
    }

    public static class Function {
        public static final String FUNCTION_DECLARATION_REGEX = "^(?:\\S*)(?: *)(.*)(?:\\()(?:.*)(?:\\)(?: *)\\{)";
        private final String functionName;
        private int declaredLine;

        public Function(String functionName, int declaredLine) {
            this.functionName = functionName;
            this.declaredLine = declaredLine;
        }

        public String getFunctionName() {
            return functionName;
        }

        public int getDeclaredLine() {
            return declaredLine;
        }

        public void setDeclaredLine(int declaredLine) {
            this.declaredLine = declaredLine;
        }
    }

    public static class Include {
        public static final String INCLUDE_REGEX = "^(?:#include(?: *)(?:\\\"|<))(.*)(?:\\\"|>)";
        private final String includeName;
        private int declaredLine;

        public Include(String includeName, int declaredLine) {
            this.includeName = includeName;
            this.declaredLine = declaredLine;
        }

        public String getIncludeName() {
            return includeName;
        }

        public int getDeclaredLine() {
            return declaredLine;
        }

        public void setDeclaredLine(int declaredLine) {
            this.declaredLine = declaredLine;
        }
    }
}
