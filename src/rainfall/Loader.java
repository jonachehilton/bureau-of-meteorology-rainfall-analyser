package rainfall;

import textio.TextIO;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class Loader {
    private static final int IDX_YEAR = 2;
    private static final int IDX_MONTH = 3;
    private static final int IDX_DAY = 4;
    private static final int IDX_RAIN = 5;
    private static String currentKey;
    private static int currentMonth;
    private static int currentYear;

    public static Station load(String directoryName, String stationName) throws LoaderException {
        String basePath = "" + directoryName + File.separator + stationName;
        String rawPath = basePath + ".csv";
        String analysedPath = basePath + "_analysed.csv";

        File analysedFile = new File(analysedPath);

        if (directoryName.length() == 0){
            throw new LoaderException("Empty directory name");
        }
        else if (stationName.length() == 0) {
            throw new LoaderException("Empty station name");
        }

        if (!Files.isRegularFile(analysedFile.toPath())) {
            calculateRainData(rawPath, analysedPath);
        }
        if (analysedFile.length() == 0) {
            throw new LoaderException("File is empty");
        }

        ArrayList<Record> records = getRecords(analysedPath);
        return new Station(records);
    }

    private static ArrayList<Record> getRecords(String analysedPath) {
        TextIO.readFile(analysedPath);
        ArrayList<Record> records = new ArrayList<>();
        int count = -1;
        while (!TextIO.eof()) {
            String row = TextIO.getln();
            String[] data = row.split(",");
            count++;
            if (count < 1) continue;// To prevent reading the column titles

            int year = Integer.parseInt(data[0]);
            int month = Integer.parseInt(data[1]);
            double total = Double.parseDouble(data[2]);
            double min = Double.parseDouble(data[3]);
            double max = Double.parseDouble(data[4]);

            records.add(new Record(year, month, total, min, max));

        }
        return records;
    }

    private static void calculateRainData(String rawPath, String dstPath) throws LoaderException {
        TextIO.readFile(rawPath);
        TextIO.writeFile(dstPath);

        HashMap<String, Double> monthlyTotals = new HashMap<>();
        HashMap<String, Double> monthlyMinimums = new HashMap<>();
        HashMap<String, Double> monthlyMaximums = new HashMap<>();

        int count = -1;
        while (!TextIO.eof()) {
            String row = TextIO.getln();
            String[] data = row.split(",");

            count++;
            if (count < 1) continue; // To prevent reading the column titles

            int year = Integer.parseInt(data[IDX_YEAR]);
            int month = Integer.parseInt(data[IDX_MONTH]);
            int day = Integer.parseInt(data[IDX_DAY]);
            double rain = data.length > 5 ? Double.parseDouble(data[IDX_RAIN]) : 0; // Handles missing rain data

            if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                throw new LoaderException("Data in the file is incorrect");
            }

            String key = "y" + year + "m" + month; // Key for all HashMaps

            if (count == 1) {
                resetValues(key, year, month);
                TextIO.put("year,month,total,min,max" + System.lineSeparator());
            }

            // Calculates total monthly rain
            double oldValue = 0;
            if (monthlyTotals.containsKey(key)) {
                oldValue = monthlyTotals.get(key);
            }
            double newValue = oldValue + rain;
            monthlyTotals.put(key, newValue);

            // Calculates minimum monthly rain
            if (monthlyMinimums.containsKey(key)) {
                oldValue = monthlyMinimums.get(key);
                if (rain < oldValue) {
                    monthlyMinimums.put(key, rain);
                }
            } else monthlyMinimums.put(key, rain);

            // Calculates maximum monthly rain
            if (monthlyMaximums.containsKey(key)) {
                oldValue = monthlyMaximums.get(key);
                if (rain > oldValue) monthlyMaximums.put(key, rain);
            } else monthlyMaximums.put(key, rain);


            if (!key.equals(currentKey)) { // Month has changed
                double total = monthlyTotals.get(currentKey);
                double min = monthlyMinimums.get(currentKey);
                double max = monthlyMaximums.get(currentKey);
                writeLineToFile(total, min, max);
                resetValues(key, year, month);
            }
        }

    }

    private static void writeLineToFile(double total, double min, double max) {
        String newLine = String.format("%d,%d,%1.2f,%1.2f,%1.2f%s", currentYear, currentMonth,
                total, min, max, System.lineSeparator());
        TextIO.putf(newLine);
    }

    private static void resetValues(String key, int year, int month) {
        currentKey = key;
        currentMonth = month;
        currentYear = year;
    }

    public static class LoaderException extends Exception
    {
        private final String errorMessage;

        public LoaderException(String message){
            super(message);
            errorMessage = message;

        }
        @Override
        public String toString(){
            return errorMessage;
        }
    }
}
