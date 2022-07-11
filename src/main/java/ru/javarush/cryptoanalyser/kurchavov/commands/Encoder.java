package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.util.PathFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.javarush.cryptoanalyser.kurchavov.entity.CurrectAlphabet.*;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class Encoder extends Action implements Executer {

    @Override
    public Result execute(String[] parameters) throws IOException {
        super.initParameters(parameters);
        String resultString = getResultString();
        return writeFile(resultPath, resultString);
    }

}
