package ru.javarush.cryptoanalyser.kurchavov.util;

import ru.javarush.cryptoanalyser.kurchavov.entity.Result;
import ru.javarush.cryptoanalyser.kurchavov.entity.ResultCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavingFile {
    public Result writeFile(Path path, String resultString ) {
        try{
            Files.writeString(path, resultString);
        }catch (IOException e) {
            return new Result(ResultCode.ERROR,"Error writing file " + path);
        }
        return new Result(ResultCode.OK,"OK writing file " + path);

    }
}
