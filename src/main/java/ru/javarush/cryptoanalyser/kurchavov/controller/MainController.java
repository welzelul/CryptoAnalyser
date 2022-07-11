package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.commands.Action;

public class MainController {
    public Result execute(String command, String[] parameters) {
        Action action = Actions.find(command);
        Result result = action.execute(parameters);
        throw new UnsupportedOperationException();
    }
}
