package stu.ilexa;

/**
 * Класс, предназначенный для хранения данных одной записи для практической работы №7
 */
public class Entry {
    private final String temperature;
    private final String humidity;
    private final String motion;
    private final String airQuality;
    private final String time;
    private final int ip;

    public Entry(String temperature, String humidity, String motion, String airQuality, String time, int ip) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.motion = motion;
        this.airQuality = airQuality;
        this.time = time;
        this.ip = ip;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getMotion() {
        return motion;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public String getTime() {
        return time;
    }

    public int getIp() {
        return ip;
    }
}
