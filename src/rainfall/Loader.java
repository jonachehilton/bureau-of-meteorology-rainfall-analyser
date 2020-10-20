package rainfall;

import textio.TextIO;

import java.io.File;
import java.nio.file.Files;
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
        String fromPath = basePath + "_analysed.csv";

        File rawFile = new File(rawPath);
        File fromFile = new File(fromPath);

        if (Files.isRegularFile(rawFile.toPath())) {
            System.out.println("FOUND raw file = " + rawFile.toPath());
        } else {
            throw new LoaderException();
        }

        if (Files.isRegularFile(fromFile.toPath())) {
            System.out.println("FOUND _analysed file = " + fromFile.toPath());
        } else {
            System.out.println("MAKING _analysed file = " + fromFile.toPath());
            calculateRainData(rawPath, fromPath);
        }
        return null;
    }


    private static void calculateRainData(String rawPath, String dstPath) {
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
                System.out.println("ERROR: failed to process file");
                return;
            }

            String key = "y" + year + "m" + month; // Key for all HashMaps

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
                if (count == 1) {
                    currentKey = key;
                    currentMonth = month;
                    currentYear = year;
                    TextIO.put("year,month,total,min,max\n");
                }
                TextIO.put(currentYear + ", " + currentMonth + ", ");
                TextIO.putf("%1.2f, ", monthlyTotals.get(currentKey));
                TextIO.put(monthlyMinimums.get(currentKey).toString() + ", " + monthlyMaximums.get(currentKey).toString());
                TextIO.putln();
                currentKey = key;
                currentMonth = month;
                currentYear = year;
            }
        }

    }


    public static class LoaderException extends Exception {
        // your LoaderException implementation here
    }
}
