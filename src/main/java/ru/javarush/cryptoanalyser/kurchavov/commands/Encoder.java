package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Encoder extends Action {
    public Encoder() {
        setDefaultParameters();
    }

    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(1, "sourcePathAsString",
                2,  "resultPathAsString",
                3, "key");
        sourcePathAsString = "sourceFile.txt";
        resultPathAsString = "encrypted.txt";
        key = 1;
    }

    @Override
    public Result execute(String[] parameters) throws IllegalAccessException {
        Result initResult = null;
        if (!isInitialized())
            initResult = initParameters(parameters);
        if (initResult != null && initResult.getResultCode() == ResultCode.ERROR)
            return initResult;
        String resultString = buildResultString();
        return writeFile(this.getResultPath(), resultString);
    }



}
