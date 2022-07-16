package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

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
        necessaryParameters = Map.of(0, "sourcePathAsString",
                1,  "resultPathAsString",
                2, "key");
        sourcePathAsString = "encrypted.txt";
        resultPathAsString = "decrypted.txt";
        key = 1;
    }
    @Override
    public Result start(){
        String resultString = buildResultString();
        setResultString(resultString);
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
