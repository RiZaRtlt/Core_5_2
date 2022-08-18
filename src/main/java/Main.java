import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.collections.collection.CompositeCollection;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main  {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String fileName = "./src/main/resources/data.xml";

        List<Employee> list = parseXML(fileName);

        if (list != null) {
            String json = listToJson(list);

            try (FileWriter file = new FileWriter("./src/main/resources/Json.json")) {
                file.write(json);
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String listToJson(List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    private static List parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        Node root = doc.getDocumentElement();

        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;

            Node childNode = nodeList.item(i);
            NodeList nodeList2 = childNode.getChildNodes();
            for (int j = 0; j < nodeList2.getLength(); j++) {
                Node node = nodeList2.item(j);
                if (node.getNodeName() == "id") {
                    String sId = node.getTextContent();
                    id = Long.parseLong(sId);
                }
                if (node.getNodeName() == "firstName") firstName = node.getTextContent();
                if (node.getNodeName() == "lastName") lastName = node.getTextContent();
                if (node.getNodeName() == "country") country = node.getTextContent();
                if (node.getNodeName() == "age") age = Integer.parseInt(node.getTextContent());
            }

            Employee emp = new Employee(id, firstName, lastName, country, age);
            if (list.add(emp)){}
        }

        return list;
    }
}
