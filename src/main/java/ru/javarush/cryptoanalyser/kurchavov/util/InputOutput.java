package ru.javarush.cryptoanalyser.kurchavov.util;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class InputOutput {
    public static Scanner scanner = new Scanner(System.in);
    public static String getRoot(){
        String root = System.getProperty("user.dir");
        return root + File.separator + "text" + File.separator;
    }

    public static Result writeFile(Path path, String stringForWrite) {
        try{
            Files.writeString(path, stringForWrite);
        } catch (IOException e){
            return new Result(ResultCode.ERROR, e.getMessage());
        }
        return new Result(ResultCode.OK, "Success writing" + path);
    }

    public static int enterInt(String subject) {
        int key = 0;
        System.out.println("Enter " + subject + " (number): ");
        String s;
        while(!(s = scanner.nextLine()).equals("")){
            try {
                key = Integer.parseInt(s);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Wrong " + subject + ". Retry please again");
            }
        }
        return key;
    }
    public static String questionSaveStringYesNo() {
        System.out.println("Save finding string?(y/n):");
        return scanner.nextLine();
    }


}
