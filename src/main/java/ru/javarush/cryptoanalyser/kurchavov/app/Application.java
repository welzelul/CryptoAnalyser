package ru.javarush.cryptoanalyser.kurchavov.app;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.controller.MainController;

import java.io.IOException;
import java.util.Arrays;

public class Application {
    private final MainController mainController;

    public Application(MainController mainController) {
        this.mainController = mainController;
    }

    public Result run(String[] args) throws IOException, IllegalAccessException {
        String command = args[0];
        String[] parameters = Arrays.copyOfRange(args, 1, args.length);
        return mainController.execute(command, parameters);
    }
}
