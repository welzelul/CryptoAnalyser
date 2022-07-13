package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Decoder extends Action {
    public Decoder() {
        setDefaultParameters();
    }

    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(1, "sourcePathAsString",
                2,  "resultPathAsString",
                3, "key");
        sourcePathAsString = "encrypted.txt";
        resultPathAsString = "decrypted.txt";
        key = 1;
    }
    @Override
    public Result execute(String[] parameters) throws IllegalAccessException {
        initParameters(parameters);
        String resultString = buildResultString();
        return writeFile(this.getResultPath(), resultString);
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
