package ru.javarush.cryptoanalyser.kurchavov.controller;

import ru.javarush.cryptoanalyser.kurchavov.commands.*;

public enum Actions {
    ENCODE(new Encoder()),
    DECODE(new Decoder()),
    BROOTFORCE(new BrootForce()),
    ANALYSE(new Analyse());
    private final Action action;
    public final int count;

    Actions(Action action) {
        count = this.ordinal();
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public static Actions getEnumByOrdinal(int ordinal){
        Actions result;
        try{
            result = Actions.values()[ordinal];
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
        return result;
    }

    public static Action getActionByName(String name) {
        return Actions.valueOf(name.toUpperCase()).action;
    }
    public static Action find(String command) {
        return Actions.valueOf(command.toUpperCase()).action;
    }
}
