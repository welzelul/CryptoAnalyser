package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;
import ru.javarush.cryptoanalyser.kurchavov.util.InputOutput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;

public abstract class Action{
    public Result execute(String[] parameters) throws IOException//input to the method
    {
        Result initResult = null;
        if (!isInitialized())
            initResult = initParameters(parameters);
        if (initResult != null && initResult.getResultCode() == ResultCode.ERROR)
            return initResult;
        return start();
    }
    abstract Result start() throws IOException; //input to the method;

    abstract char getCharFromAlphabet(char ch); // may be different in each operation
    public abstract void setDefaultParameters(); //setting default args which need to input necessary parameters in console
    private String sourceString;
    private String resultString;

    private Path sourcePath;
    private Path resultPath;
    private Path dictPath;
    private boolean Initialized;
    //Need public arguments for Reflection API. Without this modifier i dont know how it will be do TODO
    public String sourcePathAsString;
    public String resultPathAsString;
    public String dictPathAsString;
    public int key;
    public Action action;
    public String currentABC;
    public Map<Integer, String> necessaryParameters;
    public Action() {
    }
    public Map<Integer, String> getNecessaryParameters() {
        return necessaryParameters;
    }
    public String getResultString() {
        return resultString;
    }
    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setResultPath(Path resultPath) {
        this.resultPath = resultPath;
    }
    public String getSourcePathAsString() {
        return sourcePathAsString;
    }

    public String getResultPathAsString() {
        return resultPathAsString;
    }
    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getResultPath() {
        return resultPath;
    }

    public Action getAction() {
        return action;
    }

    public String getCurrentABC() {
        return currentABC;
    }

    public int getKey() {
        return key;
    }

    public void setSourcePathAsString(String sourcePathAsString) {
        this.sourcePathAsString = sourcePathAsString;
    }

    public void setResultPathAsString(String resultPathAsString) {
        this.resultPathAsString = resultPathAsString;
    }
    public void setDictPath(Path dictPath) {
        this.dictPath = dictPath;
    }
    public void setDictPathAsString(String dictPathAsString) {
        this.dictPathAsString = dictPathAsString;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setInitialized(boolean initialized) {
        Initialized = initialized;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public boolean isInitialized() {
        return Initialized;
    }
    void buildABC(){
        buildABC(key);
    }
    void buildABC(int key){
        if (key<0)
            return;
        if(key>ABC.length())
            key = key - ABC.length();
        currentABC = ABC.substring(key) + ABC.substring(0, key);
    }

    char getResultChar(char ch){
        boolean isAlphabetic = Character.isAlphabetic(ch);
        boolean isUpperCase = false;

        if (isAlphabetic)
            isUpperCase = Character.isUpperCase(ch);
        if (isUpperCase)
            ch = Character.toLowerCase(ch);

        char resultChar = getCharFromAlphabet(ch);
        if (isUpperCase)
            resultChar = Character.toUpperCase(resultChar);
        return resultChar;
    }

    String buildResultString(String sourceString){
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {

            char ch = sourceString.charAt(i);
            ch = getResultChar(ch);
            resultBuilder.append(ch);
        }
        resultString = resultBuilder.toString();
        return resultString;
    }
    String buildResultString(){
        return buildResultString(sourceString);
    }

    public void readSourceFile() throws IOException {
        if (!sourcePathAsString.isEmpty()){
            sourceString = Files.readString(sourcePath);
        }
    }
    public String getSourceString() {
        return sourceString;
    }
    public String getDictPathAsString() {
        return dictPathAsString;
    }


    public Path getDictPath() {
        return dictPath;
    }
    public void setInitialization(boolean isInitialized) {
        this.Initialized = isInitialized;
    }
    HashMap<Integer, Values> getRegularity() {
        return getRegularity(this);
    }
    HashMap<Integer, Values> getRegularity(Action action) { //build Map there most used letter is Space
        HashMap<Integer, Values> mapTemp = new HashMap<>();
        for (int i = 0; i < ABC.length(); i++) {
            buildABC(i);
            String resultString = buildResultString();
            if (resultString.length()>2000) // just for performance
                resultString = resultString.substring(0, 2000);
            int counterOfLetterO = resultString.replaceAll("[^ ]", "").length();
            mapTemp.put(i, new Values(counterOfLetterO, resultString));
        }
        return mapTemp;
    }
    HashMap<Character, Double> MapOfStatisticsLetters() { // Statictics of letters from String
        HashMap<Character, Double> temp = new HashMap<>();
        String sourceString = getSourceString().toLowerCase();
        int totalLetters = sourceString.length();
        List<Character> regs = Arrays.asList('(', ')', '[', ']', '{', '{', '\\', '^','$','|','?', '*', '+', '.', '<', '>', '-', '=', '!');
//        List<Character> ignoredLetters = Arrays.asList('\n','\t');
        String stringIgnoredLetters = "\n\t\r";
        Set<Character> chars = sourceString.
                chars().mapToObj(e -> (char)e).
                collect(Collectors.toSet());
        chars.forEach( currectCh ->{
            if (stringIgnoredLetters.indexOf(currectCh) == -1){
                String toReplace = (regs.contains(currectCh) ?"\\":"") + currectCh;
                String tempS = sourceString.replaceAll(toReplace, "");
                Double percentLetter = ((double) ((getSourceString().length() - tempS.length())) / totalLetters);
                temp.put(currectCh, percentLetter);
            }
        });
        return temp;
    }

    String replaceCharactersWithMapABC(HashMap<Character, Character> mapCharactersToReplace) {
        String sourceString = getSourceString();
        StringBuilder result = new StringBuilder();
        sourceString.chars().forEach( ch -> {
            char nextChar = (char) ch;
            char resultChar;
            boolean isAlphabetic = Character.isAlphabetic(nextChar);
            boolean isUpperCase = false;

            if (isAlphabetic)
                isUpperCase = Character.isUpperCase(nextChar);
            if (isUpperCase)
                nextChar = Character.toLowerCase(nextChar);
            try{
                resultChar = mapCharactersToReplace.get(nextChar);
            } catch (NullPointerException ex){
                resultChar = nextChar;
            }

            if (isUpperCase)
                resultChar = Character.toUpperCase(resultChar);
            result.append(resultChar);

        });
        return result.toString();
    }



    protected Result initParameters(String[] takenParameters) { // init args using Reflection API
        if (takenParameters.length == 0)
            return new Result(ResultCode.ERROR, "Null arguments for init");
        AtomicReference<Result> result = new AtomicReference<>();
        Class<? extends Action> currentClass = this.getClass();
        Field fieldParameters;
        try {
            fieldParameters = currentClass.getField("necessaryParameters");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        // field necessaryParameters is always Map<Integer, String>. im just learning and appling Reflection API
        Map<Integer, String> mapOfParameters;
        try {
            mapOfParameters = (Map<Integer, String>) fieldParameters.get(this);
        }catch (IllegalAccessException e){
            return new Result(ResultCode.ERROR, "IllegalAccessException necessaryParameters");
        }

        AtomicReference<String> nameField = new AtomicReference<>();
        mapOfParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        if (e.getKey() < takenParameters.length) { //false warning
                            String currectParameter = takenParameters[e.getKey() ];
                            if (!currectParameter.isEmpty()) {
                                Field parameterInClass = currentClass.getField(e.getValue());
                                nameField.set(parameterInClass.getName());
                                Class<?> classParameter = parameterInClass.getType();

                                if (classParameter.equals(Integer.class) | classParameter.equals(int.class)) {
                                    parameterInClass.set(this, Integer.parseInt(currectParameter));
                                    if ((Integer) parameterInClass.get(this)<0)
                                        result.set(new Result(ResultCode.ERROR, "error negative number. " +nameField.get() + " may be higher or equal 0"));
                                }
                                else if (classParameter.equals(String.class))
                                    parameterInClass.set(this, currectParameter);
                            }
                        }
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        result.set(new Result(ResultCode.ERROR, "error access field " + fieldParameters.toString()));
                    }
                });
            if (result.get() != null) //false warning
                return result.get();
        return setParametersAfterParse();
    }
    private Result setParametersAfterParse(){
        //TODO must be refactoring on reflection API..not now
        buildABC();
        if (getSourcePathAsString() != null)
            setSourcePath( Path.of(InputOutput.getRoot() + getSourcePathAsString()));
        if (getResultPathAsString() != null)
            setResultPath( Path.of(InputOutput.getRoot() + getResultPathAsString()));
        if (getDictPathAsString() != null)
            setDictPath( Path.of(InputOutput.getRoot() + getDictPathAsString()));

        try {
            String sourceString = Files.readString(sourcePath);
            setSourceString(sourceString);
        } catch (IOException e) {
            return new Result(ResultCode.ERROR, "error reading source file " + getSourcePathAsString());
        } catch (NullPointerException e) {
            return new Result(ResultCode.ERROR, "null source path");
        }
        Initialized = true;
        return new Result(ResultCode.OK);
    }


}
