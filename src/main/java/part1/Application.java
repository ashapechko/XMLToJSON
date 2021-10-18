package part1;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            XMLToJSONConverter converter = new XMLToJSONConverter();
            converter.convert("src/main/xml/example.xml", "src/main/json/example.json");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
