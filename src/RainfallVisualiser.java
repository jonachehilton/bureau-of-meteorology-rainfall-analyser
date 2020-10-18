import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rainfall.Loader;
import rainfall.Station;

public class RainfallVisualiser extends Application {

    //  add your UI control instance variables here

    private Station station;

    @Override
    public void start(Stage stage) {
        var borderPane = new BorderPane();
        var scene = new Scene(borderPane);

        // setup window particulars and make it visible
        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser");

        // add your UI control setup code here using helper methods

        stage.show();

        String stationName = "CopperlodeDamStation";
        String directoryName = "resources";
        try {
        Station station = Loader.load(directoryName, stationName);
    } catch (Loader.LoaderException e) {
            e.printStackTrace();
    }}

    public static void main(String[] args) {
        launch();
    }
}
