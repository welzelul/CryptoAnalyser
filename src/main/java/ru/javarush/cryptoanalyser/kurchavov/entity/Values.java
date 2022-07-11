package ru.javarush.cryptoanalyser.kurchavov.entity;

public class Values {
        private final int count;
        private final String resultString;

        public Values(int count, String resultString){
            this.count = count;
            this.resultString = resultString;
        }

        public int getCount() {
            return count;
        }

        public String getResultString() {
            return resultString;
        }
}
