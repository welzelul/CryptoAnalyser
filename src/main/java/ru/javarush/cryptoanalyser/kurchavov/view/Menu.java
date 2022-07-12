package ru.javarush.cryptoanalyser.kurchavov.view;

import ru.javarush.cryptoanalyser.kurchavov.commands.Action;
import ru.javarush.cryptoanalyser.kurchavov.commands.Executer;
import ru.javarush.cryptoanalyser.kurchavov.controller.Actions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Colors.*;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.scanner;

public class Menu {
    public Executer selectOperation(){
        System.out.println(ANSI_GREEN + "Enter type of operation:" + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s" + ANSI_RESET,e.ordinal(), e));
        int option = Integer.parseInt(scanner.nextLine());
        return Actions.getOperationByOrdinal(option);
    }
}
