package stu.ilexa;


import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс, предназначенный для вызова статических методов для вывода сформированных JSON и XML файлов в консоль
 */
public class FileParser {
    /**
     * Статический метод для парсинга сформированных JSON файлов
     * @param path путь к JSON файлу
     */
    public static void parseJSON(Path path) {
        try {
            String json = Files.readString(path,
                    StandardCharsets.US_ASCII);
            JSONArray arr = new JSONArray(json);
            System.out.println("JSON:");
            for (int i = 0; i < arr.length(); i++) {

                String temperature = arr.getJSONObject(i).getString("temperature");
                String illuminance = arr.getJSONObject(i).getString("illuminance");
                String motion = arr.getJSONObject(i).getString("motion");
                String sound = arr.getJSONObject(i).getString("sound");
                String time = arr.getJSONObject(i).getString("time");
                String ip = String.valueOf(arr.getJSONObject(i).getInt("ip"));
                System.out.println("    Entry " + i + ":");
                System.out.println("        Temperature: " + temperature);
                System.out.println("        Illuminance: " + illuminance);
                System.out.println("        Motion: " + motion);
                System.out.println("        Sound: " + sound);
                System.out.println("        Time: " + time);
                System.out.println("        Ip: " + ip);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Статический метод для парсинга сформированных JSON файлов
     *
     * @param path путь к JSON файлу
     */
    public static void parseXML(String path) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(path));
            System.out.println("XML:");
            NodeList entries = doc.getElementsByTagName("Entry");
            for (int i = 0; i < entries.getLength(); i++) {
                System.out.println("    Entry " + i + ":");
                System.out.println("        Temperature: " + ((Element) (entries.item(i))).getElementsByTagName("temperature").item(0).getTextContent());
                System.out.println("        Illuminance: " + ((Element) (entries.item(i))).getElementsByTagName("illuminance").item(0).getTextContent());
                System.out.println("        Motion: " + ((Element) (entries.item(i))).getElementsByTagName("motion").item(0).getTextContent());
                System.out.println("        Sound: " + ((Element) (entries.item(i))).getElementsByTagName("sound").item(0).getTextContent());
                System.out.println("        Time: " + ((Element) (entries.item(i))).getElementsByTagName("time").item(0).getTextContent());
                System.out.println("        Ip: " + ((Element) (entries.item(i))).getElementsByTagName("ip").item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
