package ru.javarush.cryptoanalyser.kurchavov.entity;

import ru.javarush.cryptoanalyser.kurchavov.constants.Colors;

import static ru.javarush.cryptoanalyser.kurchavov.constants.Colors.*;

public class Result {
    private final ResultCode resultCode;
    private final String message;

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result:\n" +
                "resultCode=" + resultCode +
                ", message='" + message;
    }

    public Result(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.message = "";
    }
    public Result(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public static void echoResult(Result result){
        String colorText = result.resultCode == ResultCode.OK ? ANSI_GREEN : ANSI_RED;
        System.out.println(colorText + "Result: " + result.resultCode.toString());
        System.out.println(colorText + "\t" + result.message);
    }
}
