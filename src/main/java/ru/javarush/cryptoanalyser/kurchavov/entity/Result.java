package ru.javarush.cryptoanalyser.kurchavov.entity;

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

    public Result(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
}
