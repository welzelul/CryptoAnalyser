package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.io.IOException;
import java.util.Map;

import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Encoder extends Action implements Executer {
    private final Map<Integer, String> necessaryParameters = Map.of(1, "sourceString",
            2,  "resultString",
            3, "keyString");
    public Encoder() {
        super();
    }

    @Override
    public Result execute(String[] parameters) throws IOException, IllegalAccessException {
        initParameters(parameters);
        String resultString = buildResultString();
        return writeFile(resultPath, resultString);
    }

}
