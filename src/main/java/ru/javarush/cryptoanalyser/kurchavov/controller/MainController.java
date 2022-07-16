package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.Action;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.exceptions.ApplicationException;

import java.io.IOException;

public class MainController{
    public Result execute(String command, String[] parameters) throws IOException, ApplicationException{
        Action action = Actions.getActionByName(command);
        if (action == null)
            return new Result(ResultCode.ERROR, "introduced unknown operation!");
        return action.execute(parameters);
    }
}
