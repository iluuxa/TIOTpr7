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

/**
 * Основной класс, содержащий выбор режима работы, методы подключения к стенду, сохранения файлов
 */
public class Main {

    /**
     * Основной метод, предназначенный для выбора режима работы и вызова соответствующих методов
     *
     * @param args аргументы командной строки. Не используются
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите режим работы программы:\n1 - сохранение данных для варианта 7 работы 7\n2 - сохранение данных для варианта 7 работы 8\n3 - Вывод сохранённых данных в консоль");
        int n = in.nextInt();
        if (n == 3) { //Вызов методов парсинга XML и JSON файлов. Чтение проводится из директории "Документы"
            FileParser.parseJSON(Paths.get(new JFileChooser().getFileSystemView().getDefaultDirectory().getPath() + "/WB-JSON.txt"));
            FileParser.parseXML(new JFileChooser().getFileSystemView().getDefaultDirectory().getPath() + "/WB-XML.xml");
        } else {
            System.out.println("Введите номер стенда:");
            int address = in.nextInt();
            IMqttClient subscriber = getConnectedSubscriber(address);
            TopicHandler topicHandler = TopicHandler.getInstance();
            if (subscriber != null) {
                switch (n) {
                    case 1 -> handleForFiles(subscriber, topicHandler, address);
                    case 2 -> handleForGraphs(subscriber, topicHandler);
                }
            }
        }
    }

    /**
     * Статический метод, предназначенный для подписки на топики и сохранения файлов для последующего их преобразования в графики для практической работы №8
     *
     * @param subscriber   экземпляр подписчика
     * @param topicHandler экземпляр класса для сохранения значений
     */
    public static void handleForGraphs(IMqttClient subscriber, TopicHandler topicHandler) {
        ArrayList<EntryGraph> entryGraphArrayList = new ArrayList<>();
        try {
            subscriber.subscribe("/devices/wb-map12e_23/controls/Ch 1 P L1", (topic, msg) -> {
                System.out.println("Voltage: " + msg);
                topicHandler.setVoltage(msg.toString());
            });
            subscriber.subscribe("/devices/wb-msw-v3_21/controls/Humidity", (topic, msg) -> {
                System.out.println("Current Motion: " + msg);
                topicHandler.setHumidity(msg.toString());
            });
            subscriber.subscribe("/devices/wb-ms_11/controls/Temperature", (topic, msg) -> {
                System.out.println("Temperature: " + msg);
                topicHandler.setTemperature(msg.toString());
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                entryGraphArrayList.add(topicHandler.getEntry8());
                StringBuilder csvString = new StringBuilder("Humidity,Temperature,Voltage\n");
                for (EntryGraph entry :
                        entryGraphArrayList) {
                    csvString.append(entry.getHumidity()).append(",").append(entry.getTemperature()).append(",").append(entry.getVoltage()).append("\n");
                }
                csvString = new StringBuilder(csvString.substring(0, csvString.length() - 1));
                try {
                    FileWriter fileWriter = new FileWriter(new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/WB-CSV.csv");
                    fileWriter.write(csvString.toString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }, 0, 5000);
    }


    /**
     * Статический метод, предназначенный для подписки на топики и сохранения файлов в формате XML и JSON
     *
     * @param subscriber   экземпляр подписчика
     * @param topicHandler экземпляр класса для сохранения значений
     * @param address      номер стенда для сохранения в записях
     */
    public static void handleForFiles(IMqttClient subscriber, TopicHandler topicHandler, int address) {
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        try {
            subscriber.subscribe("/devices/wb-msw-v3_21/controls/Current Motion", (topic, msg) -> {
                System.out.println("Current Motion: " + msg);
                topicHandler.setMotion(msg.toString());
            });
            subscriber.subscribe("devices/wb-ms_11/controls/Air Quality (VOC)", (topic, msg) -> {
                System.out.println("Sound Level: " + msg);
                topicHandler.setAirQuality(msg.toString());
            });
            subscriber.subscribe("/devices/wb-msw-v3_21/controls/Humidity", (topic, msg) -> {
                System.out.println("Illuminance: " + msg);
                topicHandler.setHumidity(msg.toString());
            });
            subscriber.subscribe("/devices/wb-m1w2_14/controls/External Sensor 1", (topic, msg) -> {
                System.out.println("Temperature: " + msg);
                topicHandler.setTemperature(msg.toString());
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    entryArrayList.add(topicHandler.getEntry(address));


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
                        illuminance.appendChild(document.createTextNode(entryArrayList.get(i).getHumidity()));
                        Element motion = document.createElement("motion");
                        motion.appendChild(document.createTextNode(entryArrayList.get(i).getMotion()));
                        Element sound = document.createElement("sound");
                        sound.appendChild(document.createTextNode(entryArrayList.get(i).getAirQuality()));
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
    }

    public static IMqttClient getConnectedSubscriber(int address) {
        try {
            IMqttClient subscriber = new MqttClient("tcp://192.168.2." + address + ":1883", UUID.randomUUID().toString());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            subscriber.connect(options);
            return subscriber;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

}
