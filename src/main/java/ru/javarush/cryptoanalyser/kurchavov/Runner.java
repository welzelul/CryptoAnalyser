package ru.javarush.cryptoanalyser.kurchavov;

import ru.javarush.cryptoanalyser.kurchavov.app.Application;
import ru.javarush.cryptoanalyser.kurchavov.controller.MainController;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

public class Runner {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        Application application = new Application(mainController);
        Result result = application.run(args);
    }
}
