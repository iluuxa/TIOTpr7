package stu.ilexa;

public class TopicHandler {
    private String temperature = "";
    private String illuminance = "";
    private String motion = "";
    private String sound = "";

    private static TopicHandler topicHandler;

    public void setTemperature(String temperature) {
        topicHandler.temperature = temperature;
    }

    public void setIlluminance(String illuminance) {
        topicHandler.illuminance = illuminance;
    }

    public void setMotion(String motion) {
        topicHandler.motion = motion;
    }

    public void setSound(String sound) {
        topicHandler.sound = sound;
    }

    public static TopicHandler getInstance() {
        if(topicHandler ==null){
            topicHandler = new TopicHandler();}
        return topicHandler;
    }

    public Entry getEntry() {
       return new Entry(topicHandler.temperature, topicHandler.illuminance, topicHandler.motion, topicHandler.sound, java.util.Calendar.getInstance().getTime().toString(), 0);
    }

}