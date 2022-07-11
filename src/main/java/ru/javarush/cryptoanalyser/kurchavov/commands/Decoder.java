package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.exceptions.ApplicationException;
import ru.javarush.cryptoanalyser.kurchavov.util.PathFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Decoder implements Action{
    @Override
    public Result execute(String[] parameters) {
        //TODO need del logic;
        String sourceFile = parameters[0];
        String resultFile = parameters[1];
        Path path = Path.of(PathFinder.getRoot() + sourceFile);
        try {
            List<String> strings = Files.readAllLines(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Result(ResultCode.OK, "read all bytes " + path);
//        throw new UnsupportedOperationException();
    }
}
