package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;
import ru.javarush.cryptoanalyser.kurchavov.entity.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.scanner;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.writeFile;

public class BrootForce extends Action{
    @Override
    public void setDefaultParameters() {
        sourcePathAsString = "encrypted.txt";
        resultPathAsString = "brutforced.txt";
        necessaryParameters = Map.of(1, "sourcePathAsString",
                2,  "resultPathAsString");
    }
    public BrootForce() {
        setDefaultParameters();
    }

    @Override
    public Result execute(String[] parameters) throws IllegalAccessException {
        //TODO need del logic;
        initParameters(parameters);

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
            this.setResultString(filtedMap.get(key).getResultString());
        } else if(filtedMap.size()>1){
            int key = filtedMap.keySet().stream().findFirst().get(); //
            this.setResultString(filtedMap.get(key).getResultString());
            //for saving.
//            filtedMap.forEach((k,v) -> {
//                String result = v.getResultString();
//                System.out.println("\t Ключ: " + k + ". " + result.substring(0,
//                        (result.length() > 50 ? 49 : result.length() - 1)) + "...\n");
//            });
//            while (true) {
//                System.out.println("Введите номер ключа, результат которого хотите сохранить: ");
//                try {
//                    int number = Integer.parseInt(scanner.nextLine());
//                    if (number < 1 || number > filtedMap.size() || filtedMap.get(number) == null)
//                        System.out.println("Неправильный вариант. Попробуйте еще раз");
//                    else {
//                        this.resultString = filtedMap.get(number).getResultString();
//                        break;
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Неправильный вариант. Попробуйте еще раз");
//                }
//            }
        }
        return writeFile(this.getResultPath(), getResultString());
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
