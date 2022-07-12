package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.*;

public enum Actions {
    ENCODE(new Encoder()),
    DECODE(new Decoder()),
    BROOTFORCE(new BrootForce()),
    ANALYSE(new Analyse());
    private final Executer action;
    public final int count;

    Actions(Executer action) {
        count = this.ordinal();
        this.action = action;
    }
    public static Executer getOperationByOrdinal(int ordinal){
        if (ordinal>Actions.values().length-1)
            throw new ArrayIndexOutOfBoundsException();
        return Actions.values()[ordinal].action;
    }

    public static Executer find(String command) {
        return Actions.valueOf(command.toUpperCase()).action;
    }
}
