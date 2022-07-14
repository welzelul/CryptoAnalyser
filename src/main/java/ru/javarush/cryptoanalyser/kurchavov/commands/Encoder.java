package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Encoder extends Action {
    public Encoder() {
        setDefaultParameters();
    }

    @Override
    char getCharFromAlphabet(char ch) {
        int indexFromAlphabet = ABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return currentABC.charAt(indexFromAlphabet);
    }

    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(0, "sourcePathAsString",
                1,  "resultPathAsString",
                2, "key");
        sourcePathAsString = "sourceFile.txt";
        resultPathAsString = "encrypted.txt";
        key = 1;
    }

    @Override
    public Result start(){
        String resultString = buildResultString();
        setResultString(resultString);
        return writeFile(this.getResultPath(), resultString);
    }



}
