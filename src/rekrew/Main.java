package rekrew;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> validOperations = new ArrayList<String>();
        validOperations.add("xtoj");
        validOperations.add("jtox");
        validOperations.add("val");

        if (args.length < 1 || !validOperations.contains(args[0])) {
            System.out.println("First param must be one of [xtoj, jtox, val]");
            return;
        }

        XMLprocessor xmlPocessor = new XMLprocessor();
        String operation = args[0];

        if (operation.equals("val")) {
            if (args.length != 3) {
                System.out.println("Please provide an XSD schema and an XML file to validate");
                return;
            }
            String xsdPath = args[1];
            String xmlPath = args[2];

            Boolean validationResult = xmlPocessor.validateXMLSchema(xsdPath, xmlPath);
            System.out.println(validationResult ? "The file is valid" : "The file is not valid");
        } else if (operation.equals("xtoj")) {
            if (args.length != 2) {
                System.out.println("Please provide an XML file to convert to JSON");
                return;
            }
            String xmlPath = args[1];
            String jsonResult = xmlPocessor.xmlToJson(xmlPath);
            System.out.println(jsonResult);
        } else if (operation.equals("jtox")) {
            if (args.length != 2) {
                System.out.println("Please provide a JSON file to convert to XML");
                return;
            }
            String jsonPath = args[1];
            String xmlResult = xmlPocessor.jsonToXml(jsonPath);
            System.out.println(xmlResult);
        }
    }
}
