package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;

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
        resultPathAsString = "brutforced.txt";
        necessaryParameters = Map.of(1, "sourcePathAsString",
                2,  "resultPathAsString");
    }
    public BruteForce() {
        setDefaultParameters();
    }

    @Override
    public Result execute(String[] parameters) throws IllegalAccessException {
        //TODO need del logic;
        initParameters(parameters);

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

        if (filtedMap.size() == 1) {
            int key = filtedMap.keySet().stream().findFirst().get(); // size == 1. at once get without check isPresent
            this.setResultString(filtedMap.get(key).getResultString());
        } else if(filtedMap.size()>1){
            int key = filtedMap.keySet().stream().findFirst().get(); //
            this.setResultString(filtedMap.get(key).getResultString());

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
