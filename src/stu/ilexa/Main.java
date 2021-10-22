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
import java.nio.file.Paths;
import java.util.*;
import java.util.Timer;

public class Main {

    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        try {
            IMqttClient subscriber = new MqttClient("tcp://192.168.2.21:1883", publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            subscriber.connect(options);
            TopicHandler topicHandler = TopicHandler.getInstance();
            ArrayList<Entry> entryArrayList = new ArrayList<>();
            ArrayList<Entry8> entry8ArrayList = new ArrayList<>();
            Scanner in = new Scanner(System.in);
            int n = 0;
            n = in.nextInt();
            switch (n) {
                case 1:
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
                                Element root = document.createElement("root");
                                document.appendChild(root);
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
                                    time.appendChild(document.createTextNode(entryArrayList.get(i).getTime()));
                                    Element ip = document.createElement("ip");
                                    ip.appendChild(document.createTextNode(String.valueOf(entryArrayList.get(i).getIp())));
                                    root.appendChild(entry);
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


                                FileWriter fileWriter = new FileWriter(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-JSON.txt");
                                fileWriter.write(new Gson().toJson(entryArrayList));
                                fileWriter.flush();
                                fileWriter.close();

                            } catch (IOException | ParserConfigurationException | TransformerException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 5000);
                    break;
                case 2:
                    subscriber.subscribe("/devices/wb-map12e_23/controls/Ch 1 P L1", (topic, msg) -> {
                        System.out.println("Voltage: " + msg);
                        topicHandler.setVoltage(msg.toString());
                    });
                    subscriber.subscribe("/devices/wb-msw-v3_21/controls/Current Motion", (topic, msg) -> {
                        System.out.println("Current Motion: " + msg);
                        topicHandler.setMotion(msg.toString());
                    });
                    subscriber.subscribe("/devices/wb-ms_11/controls/Temperature", (topic, msg) -> {
                        System.out.println("Temperature: " + msg);
                        topicHandler.setTemperature(msg.toString());
                    });
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                entry8ArrayList.add(topicHandler.getEntry8());


                                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
                                Document document = documentBuilder.newDocument();
                                Element root = document.createElement("root");
                                document.appendChild(root);
                                for (int i = 0; i < entry8ArrayList.size(); i++) {
                                    Attr number = document.createAttribute("n");
                                    number.setValue(String.valueOf(i));
                                    Element entry = document.createElement("Entry");
                                    entry.setAttributeNode(number);
                                    Element temperature = document.createElement("temperature");
                                    temperature.appendChild(document.createTextNode(entry8ArrayList.get(i).getTemperature()));
                                    Element motion = document.createElement("motion");
                                    motion.appendChild(document.createTextNode(entry8ArrayList.get(i).getMotion()));
                                    Element voltage = document.createElement("voltage");
                                    voltage.appendChild(document.createTextNode(entry8ArrayList.get(i).getVoltage()));
                                    root.appendChild(entry);
                                    entry.appendChild(temperature);
                                    entry.appendChild(motion);
                                    entry.appendChild(voltage);
                                }

                                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                                Transformer transformer = transformerFactory.newTransformer();
                                DOMSource domSource = new DOMSource(document);
                                StreamResult streamResult = new StreamResult(new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-XML-8.xml"));
                                transformer.transform(domSource, streamResult);


                                FileWriter fileWriter = new FileWriter(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-JSON-8.txt");
                                fileWriter.write(new Gson().toJson(entry8ArrayList));
                                fileWriter.flush();
                                fileWriter.close();

                            } catch (IOException | ParserConfigurationException | TransformerException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 5000);
                    break;
                case 3:
                    FileParser.parseJSON(Paths.get(new JFileChooser().getFileSystemView().getDefaultDirectory().getPath()+"/WB-JSON.txt"));
                    FileParser.parseXML(new JFileChooser().getFileSystemView().getDefaultDirectory().getPath()+"/WB-XML.xml");
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
