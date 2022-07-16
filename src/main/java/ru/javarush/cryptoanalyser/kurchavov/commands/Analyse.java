package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.util.InputOutput;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.*;

public class Analyse extends Action{
    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(0, "sourcePathAsString",
                1,  "resultPathAsString",
                2, "dictPathAsString");
        sourcePathAsString = "encrypted.txt";
        resultPathAsString = "analyzed.txt";
        dictPathAsString = "dict.txt";
        key = 0;
    }

    String compareString;
    Path comparePath;
    public Analyse() {
        setDefaultParameters();
    }

    @Override
    public Result start(){
        //TODO must remake
        Action dictForComparing = new Analyse();
        dictForComparing.setSourcePath(this.getDictPath());
        try {
            dictForComparing.readSourceFile();
        }catch (IOException e) {
           return new Result(ResultCode.ERROR, "error reading dictionary file for this operation");
        }

        HashMap<Character, Double> mapEncrypted = this.MapOfStatisticsLetters();
        HashMap<Character, Double> mapDict      = dictForComparing.MapOfStatisticsLetters();
        HashMap<Character, Character> mapCharactersToReplace = new HashMap<>();

        mapEncrypted.entrySet(). // delete insignicant letters
                forEach( e -> {
                    try {
                        Optional<Map.Entry<Character, Double>> comparison =
                                mapDict.entrySet().stream().
                                        collect(Collectors.toMap(Map.Entry::getKey, v -> Math.abs(v.getValue() - e.getValue()))).
                                        entrySet().stream().min((e1,e2)-> (int) (e1.getValue()*1000 - e2.getValue()*1000));
                        comparison.ifPresent( k -> {
                            mapCharactersToReplace.put(e.getKey(), k.getKey());
                            mapDict.remove(comparison.get().getKey());
                        });

                    }catch (NoSuchElementException ex){
                        System.out.println(e);
                    }

                });

        String resultString = replaceCharactersWithMapABC(mapCharactersToReplace);
        return writeFile(this.getResultPath(), resultString);
    }

    @Override
    public char getCharFromAlphabet(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
