import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rainfall.Loader;
import rainfall.Record;
import rainfall.Station;

import java.util.ArrayList;
import java.util.Collections;

public class RainfallVisualiser extends Application {

    private Station station;
    private Button button;
    private TextField directoryTextField;
    private TextField stationNameTextField;
    private TextArea dataArea;
    private Label statusLabel;
    private Label statusInfo;
    private Canvas canvas;
    private int width;
    private int height;

    @Override
    public void start(Stage stage) {
        width = 1000;
        height = 600;
        var borderPane = new BorderPane();
        var scene = new Scene(borderPane);
        borderPane.setPrefSize(width, height);

        stage.setScene(scene);
        stage.setTitle("Rainfall Visualiser");

        // add your UI control setup code here using helper methods
        directoryTextField = new TextField("resources");

        stationNameTextField = new TextField("CopperlodeDamStation"); // Change this back
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

        statusLabel = new Label("Status: ");
        statusInfo = new Label("Ready");
        HBox statusBox = new HBox(statusLabel, statusInfo);
        statusBox.setPadding(new Insets(5));
        borderPane.setBottom(statusBox);

        canvas = new Canvas(width, height);
        HBox canvasBox = new HBox(canvas);
        canvasBox.setPadding(new Insets(10));
        canvasBox.setSpacing(10);
        borderPane.setLeft(canvasBox);

        stage.show();

    }

    private void clickOpenButton() {
        String directoryName = directoryTextField.getText();
        String stationName = stationNameTextField.getText();
        try {
            station = Loader.load(directoryName, stationName);
            populateDataArea();
            drawPicture(canvas.getGraphicsContext2D());
            statusInfo.setText("Loaded " + stationName);
        } catch (Loader.LoaderException e) {
            e.printStackTrace();
            statusInfo.setText(e.toString()); // Update status message
        }
    }

    private void populateDataArea() {
        String stationRecords = station.toString();
        String formattedStationRecords = (stationRecords.substring(1, stationRecords.length() - 1));

        StringBuilder statistics = new StringBuilder();

        int i = 0;
        while (i < station.getYearsInStation().length) {
            String[] record = formattedStationRecords.split(" ");
            int recordLength = record[i].length();
            String formattedRecord = record[i].substring(0, recordLength - 1); // Remove trailing comma

            if (i != (station.getYearsInStation().length - 1)) {
                statistics.append(formattedRecord).append("\n");

            } else statistics.append(record[i]); // Handles final record that has no trailing comma

            i++;
        }
        dataArea.setText(statistics.toString());
    }

    private void drawPicture(GraphicsContext g) {
        g.setLineWidth(1.5);
        g.strokeLine(20, 5, 20, 600); // Create Y-axis line
        g.setLineWidth(3);
        g.strokeLine(20, 600, 970.1, 600); // Create X-axis line

        // Dynamically calculates the space between bars so that all data will always fit on the X-Axis
        double spaceBetweenBars = 950d / (double) station.getYearsInStation().length;

        ArrayList<Record> records = station.getRecords();

        // Dynamically calculates multiplier so that the all data will always fit in Y-Axis
        ArrayList<Double> monthlyTotals = new ArrayList<>();
        for (Record record : records) monthlyTotals.add(record.getTotalRain());
        double barHeightMultiplier = Collections.max(monthlyTotals) / 600d;

        double barPosition = 20.1; // X-axis position of first bar
        g.setFill(Color.CORNFLOWERBLUE);
        int i = 0;
        while (i < station.getYearsInStation().length) {
            double rainTotal = records.get(i).getTotalRain(); //
            double barHeight = rainTotal / barHeightMultiplier;

            if (i == 1) g.fillRect(barPosition, 598 - barHeight, 2, barHeight); // Create first rain bar
            barPosition += spaceBetweenBars;
            g.fillRect(barPosition, 598 - barHeight, 2, barHeight); // Create the remaining rain bars

            i++;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
