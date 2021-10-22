package stu.ilexa;

public class Entry8 {
    private final String temperature;
    private final String motion;
    private final String voltage;

    public Entry8(String temperature, String motion, String voltage) {
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
