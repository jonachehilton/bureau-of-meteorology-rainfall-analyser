package rainfall;

import java.util.ArrayList;

public class Station {
    private final ArrayList<Record> records;

    public Station(ArrayList<Record> stationData) {
        this.records = stationData;
    }

    @Override
    public String toString() {
        return records.toString();
    }

}
