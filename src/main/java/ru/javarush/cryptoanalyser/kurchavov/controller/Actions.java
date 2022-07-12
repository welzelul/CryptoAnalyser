package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.*;

public enum Actions {
    ENCODE(new Encoder()),
    DECODE(new Decoder()),
    BROOTFORCE(new BrootForce()),
    ANALYSE(new Analyse());
    private final Executer action;

    Actions(Executer action) {

        this.action = action;
    }

    public static Executer find(String command) {
        return Actions.valueOf(command.toUpperCase()).action;
    }
}
