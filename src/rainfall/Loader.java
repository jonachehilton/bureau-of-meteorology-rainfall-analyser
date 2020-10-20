package rainfall;

import textio.TextIO;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

public class Loader {

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
            makeAndSave(rawPath, fromPath);
        }
        return null;
    }


    private static void makeAndSave(String rawPath, String dstPath) {


        int count = -1;


        while (!TextIO.eof()) {
            count++;
            if (count < 1) continue; // To prevent reading the column titles

            String row = TextIO.getln();
            String[] data = row.split(",");

            int year = Integer.parseInt(data[2]);
            if (year < 1000 || year > 2020) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file.");
            }

            int month = Integer.parseInt(data[3]);
            if (month < 1 || month > 12) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file.");
            }

            int day = Integer.parseInt(data[4]);
            if (day < 1 || day > 31) {
                System.out.println("ERROR: An error occurred during runtime, failed to process file.");
            }

            double rain = data.length > 5 ? Double.parseDouble(data[5]) : 0;


        }
    }

    public static class LoaderException extends Exception {
        // your LoaderException implementation here
    }
}
