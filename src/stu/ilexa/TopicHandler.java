package stu.ilexa;

/**
 * Класс-синглтон, предназначенный для сохранения данных в течении 5 секунд и их возврата в виде записи для файла
 */
public class TopicHandler {
    private String temperature = "";
    private String humidity = "";
    private String motion = "";
    private String airQuality = "";
    private String voltage = ""; //Используется для работы №8

    private static TopicHandler topicHandler;

    public void setTemperature(String temperature) {
        topicHandler.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        topicHandler.humidity = humidity;
    }

    public void setMotion(String motion) {
        topicHandler.motion = motion;
    }

    public void setAirQuality(String airQuality) {
        topicHandler.airQuality = airQuality;
    }

    public void setVoltage(String voltage) {
        topicHandler.voltage = voltage;
    }

    public static TopicHandler getInstance() {
        if (topicHandler == null) {
            topicHandler = new TopicHandler();
        }
        return topicHandler;
    }

    /**
     * Метод, предназначенный для получения записи, в которую включены последние сохранённые данные, а также время формирования и номер стенда
     *
     * @param ip номер стенда
     * @return запись для хранения в списке и сохранения в файле
     */
    public Entry getEntry(int ip) {
        return new Entry(topicHandler.temperature, topicHandler.humidity, topicHandler.motion, topicHandler.airQuality, java.util.Calendar.getInstance().getTime().toString(), ip);
    }

    /**
     * Метод, предназначенный для получения записи, в которую включены последние сохранённые данные для практической работы №8
     *
     * @return запись для хранения в списке и сохранения в файле
     */
    public EntryGraph getEntry8() { //Используется для работы №8
        return new EntryGraph(topicHandler.temperature, topicHandler.humidity, topicHandler.voltage);
    }

}