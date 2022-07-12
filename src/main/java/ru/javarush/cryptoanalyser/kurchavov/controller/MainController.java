package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.commands.Executer;
import ru.javarush.cryptoanalyser.kurchavov.view.Menu;

import java.io.IOException;

public class MainController {
    public Result execute(String command, String[] parameters) throws IOException, IllegalAccessException {
        Menu menu = new Menu();
        Executer action = menu.selectOperation();
        Result result = action.execute(parameters);
        throw new UnsupportedOperationException();
    }
}
