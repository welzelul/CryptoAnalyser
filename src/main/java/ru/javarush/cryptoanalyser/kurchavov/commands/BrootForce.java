package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;
import ru.javarush.cryptoanalyser.kurchavov.util.PathFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.scanner;

public class BrootForce extends Action implements Executer {

    @Override
    public Result execute(String[] parameters) {
        //TODO need del logic;
        super.initParameters(parameters);

        HashMap<Integer, Values> map = this.getRegularity();
        OptionalInt maxCountOpt = map.values().stream().
                mapToInt(Values::getCount).
                max();
        int maxCount = 0;
        if (maxCountOpt.isPresent())
            maxCount = maxCountOpt.getAsInt();

        int finalMaxCount = maxCount;
        Map<Integer, Values> filtedMap = map.entrySet().stream().
                filter(e -> e.getValue().getCount() == finalMaxCount).
                        collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        if (filtedMap.size() == 1) {
            int key = filtedMap.keySet().stream().findFirst().get(); // size == 1. at once get without check isPresent
            this.resultString = filtedMap.get(key).getResultString();
        } else {
            filtedMap.forEach((k,v) -> {
                String result = v.getResultString();
                System.out.println("\t Ключ: " + k + ". " + result.substring(0,
                        (result.length() > 50 ? 49 : result.length() - 1)) + "...\n");
            });
            while (true) {
                System.out.println("Введите номер ключа, результат которого хотите сохранить: ");
                try {
                    int number = Integer.parseInt(scanner.nextLine());
                    if (number < 1 || number > filtedMap.size() || filtedMap.get(number) == null)
                        System.out.println("Неправильный вариант. Попробуйте еще раз");
                    else {
                        this.resultString = filtedMap.get(number).getResultString();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Неправильный вариант. Попробуйте еще раз");
                }
            }
        }

        return new Result(ResultCode.OK, "Done");
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
