package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;
import ru.javarush.cryptoanalyser.kurchavov.util.InputOutput;
import ru.javarush.cryptoanalyser.kurchavov.util.PathFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;

public class Action {
    String sourceString;
    String resultString;
    Path sourcePath;
    Path resultPath;
    Action action;
    String currentABC;
    int key;

    public Action(Action action, String sourceString, String resultString) {
        this.sourceString = sourceString;
        this.resultString = resultString;
        this.action = action;
        this.sourcePath = Path.of(InputOutput.getRoot() + sourceString);
        this.resultPath = Path.of(InputOutput.getRoot() + resultString);
    }

    public Action() {
    }

    public String getSourceString() {
        return sourceString;
    }

    public String getResultString() {
        return resultString;
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

    String getResultString(Action action){
        String sourceString = action.getSourceString();
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {

            char ch = sourceString.charAt(i);
            ch = getResultChar(ch);
            resultString.append(ch);
        }
        return resultString.toString();
    }
    public HashMap<Integer, Values> getRegularity() {
        return getRegularity(this);
    }
    public HashMap<Integer, Values> getRegularity(Action action) { //получаем мапу вариантов, где больше всего пробелов
        HashMap<Integer, Values> mapTemp = new HashMap<>();
        for (int i = 0; i < ABC.length(); i++) {
            buildABC(i);
            String resultString = getResultString(action);
            int counterOfLetterO = resultString.replaceAll("[^ ]", "").length();
            mapTemp.put(i, new Values(counterOfLetterO, resultString));
        }
        return mapTemp;
    }
    protected Result initParameters(String[] parameters) {
        sourcePath = Path.of(PathFinder.getRoot() + parameters[0]);
        resultPath = Path.of(PathFinder.getRoot() + parameters[1]);
        if (parameters.length>2) {
            key = Integer.parseInt(parameters[2]);
            buildABC();
        }
        try {
            sourceString = String.valueOf(Files.readAllLines(sourcePath));
        } catch (IOException e) {
            return new Result(ResultCode.ERROR, "error reading source file " + sourcePath.toString());
        }
        return new Result(ResultCode.OK);
    }
}
