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
    public String sourceString;
    public String resultString;
    public String sourceStringPath;
    public String resultStringPath;
    public String compareStringPath;
    public Path sourcePath;
    public Path resultPath;
    public int key;
    public String keyString;
    public Action action;
    public String currentABC;
    public Map<Integer, String> necessaryParameters;
    private boolean isInitialized;
    public Action() {
    }

    public Map<Integer, String> getNecessaryParameters() {
        return necessaryParameters;
    }

    public String getSourceStringPath() {
        return sourceStringPath;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public String getResultString() {
        return resultString;
    }
    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
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

    void buildABC(){
        currentABC = ABC.substring(key) + ABC.substring(0, key);
    }
    void buildABC(int key){
        currentABC = ABC.substring(key) + ABC.substring(0, key);
    }

    char getResultChar(char ch){
        boolean isAlphabetic = Character.isAlphabetic(ch);
        boolean isUpperCase = false;

        if (isAlphabetic)
            isUpperCase = Character.isUpperCase(ch);
        if (isUpperCase)
            ch = Character.toLowerCase(ch);

        char resultChar = this.getCharFromAlphabet(ch);
        if (isUpperCase)
            resultChar = Character.toUpperCase(resultChar);
        return resultChar;
    }

    char getCharFromAlphabet(char ch) {
        int indexFromAlphabet = ABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return currentABC.charAt(indexFromAlphabet);
    }

    String buildResultString(){
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {

            char ch = sourceString.charAt(i);
            ch = getResultChar(ch);
            resultBuilder.append(ch);
        }
        resultString = resultBuilder.toString();
        return resultString;
    }

    private String getSourceString() {
        return sourceString;
    }

    public HashMap<Integer, Values> getRegularity() {
        return getRegularity(this);
    }
    public HashMap<Integer, Values> getRegularity(Action action) { //получаем мапу вариантов, где больше всего пробелов
        HashMap<Integer, Values> mapTemp = new HashMap<>();
        for (int i = 0; i < ABC.length(); i++) {
            buildABC(i);
            String resultString = buildResultString();
            int counterOfLetterO = resultString.replaceAll("[^ ]", "").length();
            mapTemp.put(i, new Values(counterOfLetterO, resultString));
        }
        return mapTemp;
    }
    public static Map<Character, Integer> returnMapTopCharasters(Action action) {
        HashMap<Character, Integer> temp = new HashMap<>();
        List<Character> regs = Arrays.asList('(', ')', '[', ']', '{', '{', '\\', '^','$','|','?', '*', '+', '.', '<', '>', '-', '=', '!');
        String s = action.getSourceString().toLowerCase();
        for (int i = 0; i < ABC.length(); i++) {
            Character currectCh = ABC.charAt(i);
            String toReplace = (regs.contains(currectCh) ?"\\\\":"") + currectCh;
            String tempS = s.replaceAll(toReplace, "");
            int countLetters = action.getSourceString().length()-tempS.length();
            temp.put(currectCh, countLetters);
        }

        Optional<Integer> opt = temp.values().
                stream().max(Comparator.comparingInt(e -> e));
        int mostTimes;

        if (opt.isPresent())
            mostTimes = opt.get(); // max most times character
        else
            return temp;

        int finalMostTimes = mostTimes;

        return temp.entrySet().
                stream().filter(x -> x.getValue()> finalMostTimes - 1).
                collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue)); // all options where count characters more finalMostTimes - 1
    }
    protected Result initParameters(String[] parameters) throws IllegalAccessException {
        if (parameters.length == 0)
            return new Result(ResultCode.ERROR, "Null arguments for init");
        AtomicReference<Result> result = null;
        Class<? extends Action> currentClass = this.getClass();
        Field fieldParameters;
        try {
            fieldParameters = currentClass.getField("necessaryParameters");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, String> mapOfParameters = (Map<Integer, String>) fieldParameters.get(this);
        AtomicReference<String> nameField = null;
        mapOfParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        if (e.getKey() < parameters.length -1) {
                            String currectParameter = parameters[e.getKey()];
                            if (!currectParameter.isEmpty()) {
                                Field parameterInClass = currentClass.getDeclaredField(e.getValue());
                                nameField.set(parameterInClass.getName());
                                Class<?> classParameter = parameterInClass.getType();

                                if (classParameter.equals(Integer.class))
                                    parameterInClass.set(this, Integer.parseInt(currectParameter));
                                else if (classParameter.equals(String.class))
                                    parameterInClass.set(this, currectParameter);
                            }
                        }
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        result.set(new Result(ResultCode.ERROR, "error access field " + fieldParameters.toString()));
                    }
                });
            if (result != null && result.get() != null)
                return result.get();
        if (sourceString != null)
            sourcePath = Path.of(InputOutput.getRoot() + sourceString);
        if (resultString != null)
            resultPath = Path.of(InputOutput.getRoot() + resultString);
        try {
            sourceString = String.valueOf(Files.readAllLines(sourcePath));
        } catch (IOException e) {
            return new Result(ResultCode.ERROR, "error reading source file " + sourcePath.toString());
        }
        isInitialized = true;
        return new Result(ResultCode.OK);
    }


    public abstract Result execute(String[] parameters) throws IOException, IllegalAccessException;
    public abstract void setDefaultParameters();

    public void setInitialization(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }
}
