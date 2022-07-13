package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.Action;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.view.Menu;

import java.io.IOException;

public class MainController {
    public Result execute(String command, String[] parameters) throws IOException, IllegalAccessException {
        Action action = Actions.getActionByName(command);
        return action.execute(parameters);
    }
}
