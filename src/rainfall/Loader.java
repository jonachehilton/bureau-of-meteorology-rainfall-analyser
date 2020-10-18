package rainfall;

import java.io.File;
import java.nio.file.Files;

public class Loader {

    public static Station load(String directoryName, String stationName) throws LoaderException {
        System.out.println("directoryName = " + directoryName);
        System.out.println("stationName = " + stationName);
        String basePath = "" + directoryName + File.separator + stationName;
        String rawPath = basePath + ".csv";
        String fromPath = basePath + "_analysed.csv";
        System.out.println("rawPath = " + rawPath);
        System.out.println("fromPath = " + fromPath);

        File rawFile = new File(rawPath);
        File fromFile = new File(fromPath);

        if (Files.isRegularFile(rawFile.toPath())) {
            System.out.println("FOUND raw file = " + rawFile.toPath());
        }
        else {
            throw new LoaderException();
        }
        if (Files.isRegularFile(fromFile.toPath())) {
            System.out.println("FOUND _analysed file = " + fromFile.toPath());
        }
        else {
            System.out.println("MAKING _analysed file = " + fromFile.toPath());
            // EVENTUALLY throw new LoaderException();
        }


        return null;
    }

    public static class LoaderException extends Exception {
        // your LoaderException implementation here
    }
}
