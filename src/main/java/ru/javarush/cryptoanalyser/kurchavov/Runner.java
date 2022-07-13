package ru.javarush.cryptoanalyser.kurchavov;

import ru.javarush.cryptoanalyser.kurchavov.app.Application;
import ru.javarush.cryptoanalyser.kurchavov.controller.MainController;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.view.Menu;

import java.io.IOException;

import static ru.javarush.cryptoanalyser.kurchavov.entity.Result.echoResult;

public class Runner {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        MainController mainController = new MainController();
        Application application = new Application(mainController);

        if (args.length == 0) {
            Menu menu = new Menu();
            String operationName = menu.enterStringOperation();
            if (operationName == null)
                echoResult(new Result(ResultCode.ERROR,"empty operation"));
            args = menu.enterParameters(operationName);
            if (args == null)
                echoResult(new Result(ResultCode.ERROR,"empty parameters"));
        }

        if (args.length!=0) {
            Result result = application.run(args);
            echoResult(result);
        }
    }
}
