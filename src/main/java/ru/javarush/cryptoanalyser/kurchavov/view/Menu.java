package ru.javarush.cryptoanalyser.kurchavov.view;

import ru.javarush.cryptoanalyser.kurchavov.commands.Action;
import ru.javarush.cryptoanalyser.kurchavov.controller.Actions;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Colors.*;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.scanner;

public class Menu {
    public Action initOperation() {
        Action operation = selectOperation();
        AtomicReference<Result> result = null;
        Class<? extends Action> currentClass = operation.getClass();
        AtomicReference<String> nameField = null;
        operation.necessaryParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        String currectParameter = e.getValue();
                        Field parameterInClass = currentClass.getDeclaredField(currectParameter);
                        nameField.set(parameterInClass.getName());
                        Class<?> classParameter = parameterInClass.getType();
                        if (classParameter.equals(Integer.class))
                            parameterInClass.set(operation,
                                    enterIntParameter("Enter " + nameField.get() + " OR Press Enter by default")) ;
                        else if (classParameter.equals(String.class))
                            parameterInClass.set(this,
                                    enterStringParameter("Enter " + nameField.get() + " OR Press Enter by default"));
                        else
                            throw new IllegalArgumentException();
                    } catch (IllegalAccessException | NoSuchFieldException ex) {
                        result.set(new Result(ResultCode.ERROR, "error access field " +nameField.get()));
                    } catch (NullPointerException ex){
                        //TODO заглушка
                    }
                });
        if (result != null && result.get() != null)
            return null;
        operation.setInitialization(true);
        return operation;
    }
    public Action selectOperation(){
        System.out.println(ANSI_GREEN + "Enter type of operation:" + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s" + ANSI_RESET,e.ordinal(), e));
        int option = Integer.parseInt(scanner.nextLine());
        Action action;
        try {
            action = Actions.getEnumByOrdinal(option).getAction();
        }catch (NullPointerException e){
            action = null;
        }
        return action;
    }
    public String enterStringOperation(){
        System.out.println(ANSI_GREEN + "Enter type of operation:" + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s \n" + ANSI_RESET, e.ordinal(), e));
        int option = Integer.parseInt(scanner.nextLine());
        Actions action = Actions.getEnumByOrdinal(option);
        return action != null ? action.name() : null;
    }
    public Action enterOperation(String message){
        System.out.println(ANSI_GREEN + "Enter type of operation: " + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s" + ANSI_RESET,e.ordinal(), e));
        int option = Integer.parseInt(scanner.nextLine());
        return Actions.getEnumByOrdinal(option).getAction();
    }
    public int enterIntParameter(String message){
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s" + ANSI_RESET,e.ordinal(), e));
        return Integer.parseInt(scanner.nextLine());
    }
    public String enterStringParameter(String message){
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
        return scanner.nextLine();
    }
    public String[] enterParameters(String operationName) {
        Action operation = Actions.valueOf(operationName).getAction();
        AtomicReference<Result> result = null;
        Class<? extends Action> currentClass = operation.getClass();
        AtomicReference<String> nameField = new AtomicReference<>();
        List<String> listParameters = new ArrayList<>();
        operation.necessaryParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        String currectParameter = e.getValue();
                        Field parameterInClass = currentClass.getField(currectParameter);
                        nameField.set(parameterInClass.getName());
                        Class<?> classParameter = parameterInClass.getType();
                        listParameters.add(enterStringParameter("Enter " + nameField.get() + " OR Press Enter by default"));
                    } catch (NoSuchFieldException ex) {
//                        result.set(new Result(ResultCode.ERROR, "error access field " + nameField.get()));
                    }
                });
        if (result != null && result.get() != null)
            return null;
        listParameters.add(0, operationName);
        return listParameters.toArray(new String[listParameters.size()]);
    }
}
