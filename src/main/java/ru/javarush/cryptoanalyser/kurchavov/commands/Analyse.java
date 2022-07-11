package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.entity.CurrectAlphabet.alphabetForUse;

public class Analyse extends Action implements Executer {

    @Override
    public Result execute(String[] parameters) {
        //TODO need del logic;
        throw new UnsupportedOperationException();
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
