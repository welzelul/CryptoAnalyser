package ru.javarush.cryptoanalyser.kurchavov.view;

import ru.javarush.cryptoanalyser.kurchavov.commands.Action;
import ru.javarush.cryptoanalyser.kurchavov.controller.Actions;
import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Colors.*;
import static ru.javarush.cryptoanalyser.kurchavov.entity.Result.echoResult;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.scanner;

public class Menu {

    public String[] getArgs(){
        String[] args = new String[0];
        String operationName = enterStringOperation();

        if (operationName == null)
            echoResult(new Result(ResultCode.ERROR,"empty operation"));
        else
            args = enterParameters(operationName);

        if (args.length == 0)
            echoResult(new Result(ResultCode.ERROR,"empty parameters"));

        return args;
    }
    public Action initOperation() {
        Action operation = selectOperation();
        AtomicReference<Result> result = new AtomicReference<>();
        Class<? extends Action> currentClass = operation.getClass();
        AtomicReference<String> nameField = new AtomicReference<>();
        operation.necessaryParameters.entrySet().stream().
                sorted(Comparator.comparingInt(Map.Entry::getKey)).
                forEach( e -> {
                    try {
                        String currectParameter = e.getValue();
                        Field parameterInClass = currentClass.getDeclaredField(currectParameter);
                        nameField.set(parameterInClass.getName());
                        Class<?> classParameter = parameterInClass.getType();
                        if (classParameter.equals(Integer.class)) {
                            parameterInClass.set(operation,
                                    enterIntParameter("Enter " + nameField.get() + " OR Press Enter by default"));
                            }
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
        if (result.get() != null)
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
            action = Actions.getEnumByOrdinal(option).getAction(); //we will catch possible exception
        }catch (NullPointerException e){
            return null;
        }
        return action;
    }
    public String enterStringOperation (){
        System.out.println(ANSI_GREEN + "Enter type of operation:" + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s \n" + ANSI_RESET, e.ordinal() + 1, e));
        try{
            int option = Integer.parseInt(scanner.nextLine()) - 1;
            Actions action = Actions.getEnumByOrdinal(option);
            return action != null ? action.name() : null;
        }catch (NumberFormatException exception) {
            return null;
        }
    }
    public Action enterOperation(String message){
        System.out.println(ANSI_GREEN + "Enter type of operation: " + ANSI_RESET);
        Arrays.stream(Actions.values()).
                forEach(e -> System.out.printf(ANSI_WHITE + "\t %d. %s" + ANSI_RESET,e.ordinal(), e));
        try{
            int option = Integer.parseInt(scanner.nextLine());
            if (Actions.getEnumByOrdinal(option) != null) {
                return Actions.getEnumByOrdinal(option).getAction(); //we will catch possible exception
            }
        }catch (NumberFormatException exception) {
            return null;
        }
        return null;
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
        AtomicReference<Result> result = new AtomicReference<>();
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
                        String defaultParameter = null;
                        if (parameterInClass.getType().equals(int.class) | parameterInClass.getType().equals(Integer.class))
                            defaultParameter = String.valueOf(parameterInClass.get(operation));
                        else if (parameterInClass.getType().equals(String.class))
                            defaultParameter = (String) parameterInClass.get(operation);
                        Class<?> classParameter = parameterInClass.getType();
                        String gottenParameter = enterStringParameter("Enter " + nameField.get() +
                                " OR press Enter to choose default value(" + defaultParameter + ")" );
                        listParameters.add(gottenParameter);
                    } catch (NoSuchFieldException | IllegalAccessException ex) {
//                        result.set(new Result(ResultCode.ERROR, "error access field " + nameField.get()));
                    }
                });
        if (result.get() != null)
            return null;
        listParameters.add(0, operationName);
        return listParameters.toArray(new String[0]);
    }
}
