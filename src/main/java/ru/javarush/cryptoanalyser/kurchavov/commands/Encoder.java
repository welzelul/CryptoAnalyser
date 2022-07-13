package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.io.IOException;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Encoder extends Action {
//    private String sourceString = "file.txt";
//    public Map<Integer, String> necessaryParameters = Map.of(1, "sourceString",
//            2,  "resultString",
//            3, "keyString");

    public Encoder() {
        setDefaultParameters();
    }

    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(1, "sourceString",
                2,  "resultString",
                3, "key");
        sourceString = "file.txt";
        resultString = "encoded.txt";
        key = 0;
    }

    @Override
    public Result execute(String[] parameters) throws IOException, IllegalAccessException {
        Result initResult = null;
        if (!isInitialized())
            initResult = initParameters(parameters);
        if (initResult != null && initResult.getResultCode() == ResultCode.ERROR)
            return initResult;
        String resultString = buildResultString();
        return writeFile(resultPath, resultString);
    }



}
