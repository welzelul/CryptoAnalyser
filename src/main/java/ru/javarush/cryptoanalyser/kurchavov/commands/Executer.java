package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.io.IOException;

public interface Executer {
    Result  execute(String[] parameters) throws IOException;
}
