package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.*;

public enum Actions {
    ENCODE(new Encoder()),
    DECODE(new Decoder()),
    BROOTFORCE(new BrootForce()),
    ANALYSE(new Analyse());
    private final Action action;

    Actions(Action action) {
        this.action = action;
    }

    public static Action find(String command) {
        return Actions.valueOf(command.toUpperCase()).action;
    }
}
