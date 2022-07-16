package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class BruteForce extends Action{
    @Override
    public void setDefaultParameters() {
        sourcePathAsString = "encrypted.txt";
        resultPathAsString = "bruteforced.txt";
        necessaryParameters = Map.of(0, "sourcePathAsString",
                1,  "resultPathAsString");
    }
    public BruteForce() {

        setDefaultParameters();
    }

    @Override
    public Result start(){
        //TODO
        HashMap<Integer, Values> map = this.getRegularity();
        OptionalInt maxCountOpt = map.values().stream().
                mapToInt(Values::getCount).
                max();
        int maxCount = 0;
        if (maxCountOpt.isPresent())
            maxCount = maxCountOpt.getAsInt();

        int finalMaxCount = maxCount;
        Map<Integer, Values> filtedMap = map.entrySet().stream().
                filter(e -> e.getValue().getCount() == finalMaxCount).
                        collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        if (filtedMap.size() > 0) {
            int key = filtedMap.keySet().stream().findFirst().get(); // size == 1. at once get without check isPresent
            this.setKey(key); //again build result string. because we parsed string only 2000 length
            buildABC();
            this.buildResultString();
        }
        return writeFile(this.getResultPath(), getResultString());
    }
    @Override
    public char getCharFromAlphabet(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
