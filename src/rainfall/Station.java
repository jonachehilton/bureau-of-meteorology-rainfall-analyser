package rainfall;

import java.util.ArrayList;

public class Station {
    private ArrayList<Record> records;

    public Station(ArrayList<Record> stationData) {
        setRecords(stationData);
    }

    public void setRecords(ArrayList<Record> stationData) {
        this.records = stationData;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public int[] getYearsInStation() {
        return new int[this.records.size()];
    }

    @Override
    public String toString() {
        return records.toString();
    }


}
