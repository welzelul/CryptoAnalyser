package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.commands.Executer;

import java.io.IOException;

public class MainController {
    public Result execute(String command, String[] parameters) throws IOException, IllegalAccessException {
        Executer action = Actions.find(command);
        Result result = action.execute(parameters);
        throw new UnsupportedOperationException();
    }
}
