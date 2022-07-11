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

    public static Result writeFile(Path path, String stringForWrite) throws IOException {
        Files.writeString(path, stringForWrite);
        return new Result(ResultCode.OK, "Success");
    }

}
