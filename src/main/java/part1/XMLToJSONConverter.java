package part1;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XMLToJSONConverter {
    private final DocumentBuilderFactory builderFactory;
    private final DocumentBuilder builder;

    public XMLToJSONConverter() throws ParserConfigurationException {
        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
    }

    public void convert(final String pathToXml, final String pathToJson) throws IOException, SAXException {
        final Document document = builder.parse(new File(pathToXml));
        document.normalizeDocument();

        if (document.hasChildNodes()) {
            final JsonObject jsonObject = convertChild(document.getDocumentElement().getChildNodes());
            try {
                final FileWriter fileWriter = new FileWriter(pathToJson);
                fileWriter.write(jsonObject.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JsonObject convertChild(final NodeList nodeList) {
        final JsonObject jsonObject = new JsonObject();
        for (int k = 0; k < nodeList.getLength(); k++) {
            final Node tempNode = nodeList.item(k);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.hasChildNodes() && tempNode.getChildNodes().getLength() == 1 && !tempNode.hasAttributes()) {
                    // Node без атрибутов с единственным дочерним Node - это параметр
                    jsonObject.addProperty(tempNode.getNodeName(), tempNode.getFirstChild().getNodeValue());
                } else {
                    // Сложный объект
                    JsonObject tempObject = new JsonObject();
                    if (tempNode.hasChildNodes() && tempNode.getChildNodes().getLength() > 1) {
                        // Обрабатываем дочерние элементы
                        tempObject = convertChild(tempNode.getChildNodes());
                    }

                    if (tempNode.hasAttributes()) {
                        // Обрабатываем атрибуты
                        final NamedNodeMap attributes = tempNode.getAttributes();
                        for (int i = 0; i < attributes.getLength(); i++) {
                            tempObject.addProperty(attributes.item(i).getNodeName(), attributes.item(i).getFirstChild().getNodeValue());
                        }
                    }

                    // если элемент уже присутствует - значит это JsonArray, добавляем элемент в него
                    if (jsonObject.has(tempNode.getNodeName())) {
                        jsonObject.getAsJsonArray(tempNode.getNodeName()).add(tempObject);
                    } else {
                        // если один из Node на том же уровне имеет то же имя - формируем JsonArray
                        boolean isArray = false;
                        for (int i = k + 1; i < nodeList.getLength(); i++) {
                            if (nodeList.item(i).getNodeName().equals(tempNode.getNodeName())) {
                                isArray = true;
                                break;
                            }
                        }
                        if (isArray) {
                            final JsonArray tempArray = new JsonArray();
                            tempArray.add(tempObject);
                            jsonObject.add(tempNode.getNodeName(), tempArray);
                        } else { // в противном случае просто JsonObject
                            jsonObject.add(tempNode.getNodeName(), tempObject);
                        }
                    }
                }
            }
        }
        return jsonObject;
    }
}
