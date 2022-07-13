package ru.javarush.cryptoanalyser.kurchavov.commands;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Strings.ABC;
import static ru.javarush.cryptoanalyser.kurchavov.util.InputOutput.*;

public class Analyse extends Action{
    @Override
    public void setDefaultParameters() {
        necessaryParameters = Map.of(1, "sourcePathAsString",
                2,  "resultPathAsString",
                3, "dictPathAsString");
        sourcePathAsString = "decrypted.txt";
        resultPathAsString = "encrypted.txt";
        dictPathAsString = "dict.txt";
        key = 0;
    }

    String compareString;
    Path comparePath;
    public Analyse() {
        setDefaultParameters();
    }

    @Override
    public Result execute(String[] parameters) throws IOException, IllegalAccessException {
        //TODO must do remake
        initParameters(parameters);
        Action dictForComparing = new Encoder();
        dictForComparing.setSourcePath(Path.of(sourcePathAsString));
        dictForComparing.readSourceFile();

        HashMap<Character, Double> mapEncrypted = this.MapOfStatisticsLetters();
        HashMap<Character, Double> mapDict      = dictForComparing.MapOfStatisticsLetters();
        HashMap<Character, Character> mapCharactersToReplace = new HashMap<>();

        mapEncrypted.entrySet().stream().
                sorted((e1,e2) -> (int) (e2.getValue() - e1.getValue())*100).
                forEach( e -> {

                    Optional<Map.Entry<Character, Double>> comparison =
                            mapDict.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            e2 -> e2.getValue() - e.getValue()
                                    )).entrySet().stream().max((e1,e2) ->{
                                        double v = (e2.getValue()-e1.getValue())*1000;
                                        return (int) v;
                                    });
                    comparison.ifPresent(characterDoubleEntry -> mapCharactersToReplace.put(e.getKey(), characterDoubleEntry.getKey()));
                });

//        Action fileForComparing = new Encoder();
//        fileForComparing.setSourceString(compareString);
//
//
//
//
//        Map<Character, Integer> mapNoNCrypto = returnMapTopCharasters(fileForComparing);
//        Map<Character, Integer> mapCrypto = returnMapTopCharasters(this);
//        // found the symbol that is most repeated in the text and is included in our alphabet. looking for the key.
//
//        TreeMap<Integer, String> possibleKeys = new TreeMap<>();
//
//        mapNoNCrypto.forEach((keyNoNCrypto, vN) -> {
//            int possibleOriginal = ABC.indexOf(keyNoNCrypto);
//            mapCrypto.forEach((keyCrypto,v) ->{
//                int possibleCOdedSymbol = ABC.indexOf(keyCrypto);
//                int possibleKey;
//                if (possibleCOdedSymbol>possibleOriginal)
//                    possibleKey = possibleCOdedSymbol - possibleOriginal;
//                else
//                    possibleKey = (ABC.length()-possibleOriginal) + possibleCOdedSymbol;
//                buildABC(possibleKey);
//                String possibleResult = buildResultString();
//                possibleKeys.put(possibleKey, possibleResult);
//                System.out.printf("Possible key: %s .Result: %s \n", possibleKey, possibleResult);
//            });
//        });
//        System.out.println("\nSelect key, with which the text is readable (if all are unreadable - press Enter: ");
//        int key;
//        while (true) {
//            key = enterInt("option");
//            if (key==0)
//                break;
//            else if (possibleKeys.get(key) == null) {
//                System.out.println("there is not result for this key. Retry again");
//            }else {
//                System.out.printf("Result: %s .\n", possibleKeys.get(key));
//                String answer = questionSaveStringYesNo();
//                if (answer.contains("y"))
//                    break;
//            }
//        }
//
//        buildABC(key);
        String resultString = replaceCharactersWithMapABC(mapCharactersToReplace);
        return writeFile(this.getResultPath(), resultString);
    }

    private String replaceCharactersWithMapABC(HashMap<Character, Character> mapCharactersToReplace) {
        String sourceString = getSourceString();
        StringBuilder result = null;
        sourceString.chars().forEach( ch -> result.append(mapCharactersToReplace.get(ch)));
        return result.toString();
    }

    @Override
    public char getResultChar(char ch) {
        int indexFromAlphabet = currentABC.indexOf(ch);
        if (indexFromAlphabet == -1)
            return ch;
        return ABC.charAt(indexFromAlphabet);
    }
}
