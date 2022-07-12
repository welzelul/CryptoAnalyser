package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.enterInt;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.questionSaveStringYesNo;

public class Analyse extends Action implements Executer {
    private final Map<Integer, String> necessaryParameters = Map.of(1, "sourceStringPath",
            2,  "resultStringPath",
            3, "compareStringPath");

    String compareString;
    Path comparePath;
    public Analyse() {
        super();
    }

    @Override
    public Result execute(String[] parameters) throws IllegalAccessException {
        initParameters(parameters);
        Action fileForComparing = new Action();
        fileForComparing.setSourceString(compareString);
        Map<Character, Integer> mapNoNCrypto = returnMapTopCharasters(fileForComparing);
        Map<Character, Integer> mapCrypto = returnMapTopCharasters(this);
        // found the symbol that is most repeated in the text and is included in our alphabet. looking for the key.

        TreeMap<Integer, String> possibleKeys = new TreeMap<>();

        mapNoNCrypto.forEach((keyNoNCrypto, vN) -> {
            int possibleOriginal = ABC.indexOf(keyNoNCrypto);
            mapCrypto.forEach((keyCrypto,v) ->{
                int possibleCOdedSymbol = ABC.indexOf(keyCrypto);
                int possibleKey;
                if (possibleCOdedSymbol>possibleOriginal)
                    possibleKey = possibleCOdedSymbol - possibleOriginal;
                else
                    possibleKey = (ABC.length()-possibleOriginal) + possibleCOdedSymbol;
                buildABC(possibleKey);
                String possibleResult = buildResultString();
                possibleKeys.put(possibleKey, possibleResult);
                System.out.printf("Possible key: %s .Result: %s \n", possibleKey, possibleResult);
            });
        });
        System.out.println("\nSelect key, with which the text is readable (if all are unreadable - press Enter: ");
        int key;
        while (true) {
            key = enterInt("option");
            if (key==0)
                break;
            else if (possibleKeys.get(key) == null) {
                System.out.println("there is not result for this key. Retry again");
            }else {
                System.out.printf("Result: %s .\n", possibleKeys.get(key));
                String answer = questionSaveStringYesNo();
                if (answer.contains("y"))
                    break;
            }
        }

        buildABC(key);
        resultString = getResultString();
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
