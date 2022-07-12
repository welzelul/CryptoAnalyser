package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;
import ru.javarush.cryptoanalyser.kurchavov.util.PathFinder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;

public class Action {
    String sourceString;
    String resultString;
    int key;
    Path sourcePath;
    Path resultPath;
    Action action;
    String currentABC;

    private Map<Integer, String> necessaryParameters;
    public Action() {
    }

    public String getSourceString() {
        return sourceString;
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
        String sourceString = getSourceString();
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {

            char ch = sourceString.charAt(i);
            ch = getResultChar(ch);
            resultBuilder.append(ch);
        }
        resultString = resultBuilder.toString();
        return resultString;
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
                stream().max( (e1,e2) -> e1 -e2);
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
        if (parameters.length==0)
            return new Result(ResultCode.ERROR, "Null arguments for init");
        AtomicReference<Result> result = null;
        Class<? extends Action> currentClass = this.getClass();
        Field fieldParameters;
        try {
            fieldParameters = currentClass.getDeclaredField("necessaryParameters");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, String> mapOfParameters = (Map<Integer, String>) fieldParameters.get(this);
        AtomicReference<String> nameField = null;
        mapOfParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        if (parameters.length - 1 < e.getKey()) {
                            String currectParameter = parameters[e.getKey()];
                            Field parameterInClass = currentClass.getDeclaredField(currectParameter);
                            nameField.set(parameterInClass.getName());
                            Class<?> classParameter = parameterInClass.getType();
                            Class<Integer> integerClass = Integer.class;

                            if (classParameter.equals(integerClass))
                                parameterInClass.set(this, Integer.parseInt(currectParameter));
                            else if (classParameter.equals(String.class))
                                parameterInClass.set(this, currectParameter);
                        }
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        result.set(new Result(ResultCode.ERROR, "error access field " + fieldParameters.toString()));
                    }
                });
            if (result != null && result.get() != null)
                return result.get();
        if (sourceString != null)
            sourcePath = Path.of(PathFinder.getRoot() + sourceString);
        if (resultString != null)
            resultPath = Path.of(PathFinder.getRoot() + resultString);
        buildABC();

        try {
            sourceString = String.valueOf(Files.readAllLines(sourcePath));
        } catch (IOException e) {
            return new Result(ResultCode.ERROR, "error reading source file " + sourcePath.toString());
        }
        return new Result(ResultCode.OK);
    }
}
