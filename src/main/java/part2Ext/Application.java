package part2Ext;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import part2Ext.generated.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(Library.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Library library = (Library) unmarshaller.unmarshal(new FileReader("src/main/xml/example2.xml"));
            System.out.println(library);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(library, new FileWriter("src/main/xml/example2Ext_out.xml"));

            ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
            objectMapper.writeValue(new FileWriter("src/main/json/example2Ext.json"), library);

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

    }
}
