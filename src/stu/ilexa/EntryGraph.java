package stu.ilexa;

/**
 * Класс, предназначенный для хранения данных одной записи для практической работы №8
 */
public class EntryGraph {
    private final String temperature;
    private final String humidity;
    private final String voltage;

    public EntryGraph(String temperature, String humidity, String voltage) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.voltage = voltage;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getVoltage() {
        return voltage;
    }
}
