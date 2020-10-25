package rainfall;

public class Record {
    private int year;
    private int month;
    private double totalRain;
    private double minRain;
    private double maxRain;

    public Record(int year, int month, double total, double min, double max) {
        setYear(year);
        setMonth(month);
        setTotalRain(total);
        setMinRain(min);
        setMaxRain(max);
    }

    @Override
    public String toString() {
        return year + "," + month + "," + totalRain + "," + minRain + "," + maxRain;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setTotalRain(double total) {
        this.totalRain = total;
    }

    public void setMinRain(double minRain) {
        this.minRain = minRain;
    }

    public void setMaxRain(double maxRain) {
        this.maxRain = maxRain;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public double getTotalRain() {
        return totalRain;
    }


}
