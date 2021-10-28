package stu.ilexa;

/**
 * Класс, предназначенный для хранения данных одной записи для практической работы №8
 */
public class EntryGraph {
    private final String temperature;
    private final String motion;
    private final String voltage;

    public EntryGraph(String temperature, String motion, String voltage) {
        this.temperature = temperature;
        this.motion = motion;
        this.voltage = voltage;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getMotion() {
        return motion;
    }

    public String getVoltage() {
        return voltage;
    }
}
