import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import TriResultsJava.Config;


public class ColumnsConfigParser {

    public List<TriResultsJava.Column> parse(String filename) throws IOException, BadConfigurationException {

        String content = new String(Files.readAllBytes(Paths.get(filename)));

        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            document = builder.parse(filename);
        } catch (SAXException | ParserConfigurationException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        // validate xml document
//        Schema schema = null;
//        try {
//            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
//            SchemaFactory factory = SchemaFactory.newInstance(language);
//            schema = factory.newSchema(new File(filename));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Validator validator = schema.newValidator();
//        validator.validate(new DOMSource(document));

        List<TriResultsJava.Column> columns = new ArrayList<TriResultsJava.Column>();
        TriResultsJava.Column column = null;

        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("column");
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;

                String name = eElement.getAttribute("name");
                Integer order = Integer.parseInt(eElement.getAttribute("order"));


                Element mapFromNode = TriResultsJava.XMLUtils.getChild(eElement, "mapfrom");
                List<String> altNames = TriResultsJava.XMLUtils.extractTextChildren(mapFromNode, "altname");

                column = new TriResultsJava.Column(name, order, altNames);
                columns.add(column);
            }
        }

        // check duplicates
        Map<String,String> checkAltNames = new HashMap<String, String>();
        for (TriResultsJava.Column col : columns) {
            for (String alternativeName : col.AlternativeNames()) {
                if (checkAltNames.containsKey(alternativeName)) {
                    throw new BadConfigurationException("Duplicate column name in configuration: " + alternativeName);
                }
            }
        }

        return columns;
    }

}