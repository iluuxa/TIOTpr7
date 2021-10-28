package stu.ilexa;

/**
 * Класс, предназначенный для хранения данных одной записи для практической работы №7
 */
public class Entry {
    private final String temperature;
    private final String illuminance;
    private final String motion;
    private final String sound;
    private final String time;
    private final int ip;

    public Entry(String temperature, String illuminance, String motion, String sound, String time, int ip) {
        this.temperature = temperature;
        this.illuminance = illuminance;
        this.motion = motion;
        this.sound = sound;
        this.time = time;
        this.ip = ip;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIlluminance() {
        return illuminance;
    }

    public String getMotion() {
        return motion;
    }

    public String getSound() {
        return sound;
    }

    public String getTime() {
        return time;
    }

    public int getIp() {
        return ip;
    }
}
