package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.io.IOException;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Decoder extends Action {
//    public Map<Integer, String> necessaryParameters = Map.of(1, "sourceString",
//            2,  "resultString",
//            3, "key");

    public Decoder() {
        setDefaultParameters();
    }

    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(1, "sourceString",
                2,  "resultString",
                3, "key");
        sourceString = "encoded.txt";
        resultString = "decoded.txt";
        key = 0;
    }
    @Override
    public Result execute(String[] parameters) throws IOException, IllegalAccessException {
        initParameters(parameters);
        resultString = buildResultString();
        return writeFile(resultPath, resultString);
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
