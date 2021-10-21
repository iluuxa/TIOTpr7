package stu.ilexa;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        try {
            IMqttClient subscriber = new MqttClient("tcp://192.168.2.27:1883", publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            subscriber.connect(options);
            TopicHandler topicHandler = TopicHandler.getInstance();
            ArrayList<Entry> entryArrayList = new ArrayList<>();
            subscriber.subscribe("/devices/wb-msw-v3_21/controls/Current Motion", (topic, msg) -> {
                System.out.println("Current Motion: " + msg);
                topicHandler.setMotion(msg.toString());
            });
            subscriber.subscribe("/devices/wb-msw-v3_21/controls/Sound Level", (topic, msg) -> {
                System.out.println("Sound Level: " + msg);
                topicHandler.setSound(msg.toString());
            });
            subscriber.subscribe("/devices/wb-ms_11/controls/Illuminance", (topic, msg) -> {
                System.out.println("Illuminance: " + msg);
                topicHandler.setIlluminance(msg.toString());
            });
            subscriber.subscribe("/devices/wb-ms_11/controls/Temperature", (topic, msg) -> {
                System.out.println("Temperature: " + msg);
                topicHandler.setTemperature(msg.toString());
            });
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        entryArrayList.add(topicHandler.getEntry());


                        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                        Document document = documentBuilder.newDocument();
                        for (int i = 0; i < entryArrayList.size(); i++) {
                            Attr number = document.createAttribute("n");
                            number.setValue(String.valueOf(i));
                            Element entry = document.createElement("Entry");
                            entry.setAttributeNode(number);
                            Element temperature = document.createElement("temperature");
                            temperature.appendChild(document.createTextNode(entryArrayList.get(i).getTemperature()));
                            Element illuminance = document.createElement("illuminance");
                            illuminance.appendChild(document.createTextNode(entryArrayList.get(i).getIlluminance()));
                            Element motion = document.createElement("motion");
                            motion.appendChild(document.createTextNode(entryArrayList.get(i).getMotion()));
                            Element sound = document.createElement("sound");
                            sound.appendChild(document.createTextNode(entryArrayList.get(i).getSound()));
                            Element time = document.createElement("time");
                            sound.appendChild(document.createTextNode(entryArrayList.get(i).getTime()));
                            Element ip = document.createElement("ip");
                            sound.appendChild(document.createTextNode(String.valueOf(entryArrayList.get(i).getIp())));
                            document.appendChild(entry);
                            entry.appendChild(temperature);
                            entry.appendChild(illuminance);
                            entry.appendChild(motion);
                            entry.appendChild(sound);
                            entry.appendChild(time);
                            entry.appendChild(ip);
                        }

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource domSource = new DOMSource(document);
                        StreamResult streamResult = new StreamResult(new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-XML.xml"));
                        transformer.transform(domSource, streamResult);


                        new FileWriter(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-JSON.txt")
                                .write(new Gson().toJson(entryArrayList));
                    } catch (IOException | ParserConfigurationException | TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 5000);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
