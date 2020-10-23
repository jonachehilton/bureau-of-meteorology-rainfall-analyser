import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import rainfall.Loader;
import rainfall.Station;

public class RainfallVisualiser extends Application {

    private Station station;
    private Button button;
    private TextField directoryTextField;
    private TextField stationNameTextField;
    private TextArea dataArea;

    @Override
    public void start(Stage stage) {
        var borderPane = new BorderPane();
        var scene = new Scene(borderPane);
        borderPane.setPrefSize(1000, 400);

        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser");

        // add your UI control setup code here using helper methods
        directoryTextField = new TextField("resources");

        stationNameTextField = new TextField("CopperlodeDamStation");
        stationNameTextField.setPrefWidth(400);

        button = new Button("Open");
        button.setOnAction(e -> clickOpenButton());

        HBox textAndButtonBox = new HBox(directoryTextField, stationNameTextField, button);
        HBox.setHgrow(directoryTextField, Priority.ALWAYS);
        HBox.setHgrow(stationNameTextField, Priority.ALWAYS);
        textAndButtonBox.setPadding(new Insets(10));
        textAndButtonBox.setSpacing(10);
        borderPane.setTop(textAndButtonBox);

        dataArea = new TextArea();
        dataArea.setPrefWidth(350);
        dataArea.setEditable(false);


        HBox dataBox = new HBox(dataArea);
        dataBox.setPadding(new Insets(10));
        borderPane.setRight(dataBox);

        stage.show();

    }

    private void clickOpenButton() {
        String directoryName = directoryTextField.getText();
        String stationName = stationNameTextField.getText();
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
