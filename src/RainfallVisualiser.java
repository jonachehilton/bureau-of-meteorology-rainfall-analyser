import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        borderPane.setPrefSize(1000,400);
        // setup window particulars and make it visible
        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser");

        Button button = new Button("Open");
        button.setOnAction( e -> clickOpenButton());

        HBox hbox = new HBox(button);
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        borderPane.setTop(hbox);

        stage.show();

}

    private void clickOpenButton() {
        String stationName = "CopperlodeDamStation";
        String directoryName = "resources";
        try {
            station = Loader.load(directoryName, stationName);
        } catch (Loader.LoaderException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
