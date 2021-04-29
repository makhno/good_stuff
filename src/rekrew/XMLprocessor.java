package rekrew;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLprocessor {
    public boolean validateXMLSchema(String xsdPath, String xmlPath){

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }

    public String xmlToJson(String xmlPath){

        try {
            List<Contact> contacts = new ArrayList<Contact>();
            Contact contact = null;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlPath));
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("Contact");
            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element cElement = (Element) node;
                    //Create new Employee Object
                    contact = new Contact();
                    contact.setCustomerID(extractTextByTag(cElement,"CustomerID"));
                    contact.setCompanyName(extractTextByTag(cElement,"CompanyName"));
                    contact.setContactName(extractTextByTag(cElement,"ContactName"));
                    contact.setContactTitle(extractTextByTag(cElement,"ContactTitle"));
                    contact.setAddress(extractTextByTag(cElement,"Address"));
                    contact.setCity(extractTextByTag(cElement,"City"));
                    contact.setEmail(extractTextByTag(cElement,"Email"));
                    contact.setPostalCode(extractTextByTag(cElement,"PostalCode"));
                    contact.setCountry(extractTextByTag(cElement,"Country"));
                    contact.setPhone(extractTextByTag(cElement,"Phone"));
                    contact.setFax(extractTextByTag(cElement,"Fax"));

                    //Add Employee to list
                    contacts.add(contact);
                }
            }

            JSONArray addressBook = new JSONArray();
            for (Contact currContact : contacts) {
                JSONObject contactAsJsonObj = new JSONObject();
                contactAsJsonObj.put("CustomerID", currContact.getCustomerID());
                contactAsJsonObj.put("CompanyName", currContact.getCompanyName());
                contactAsJsonObj.put("ContactName", currContact.getContactName());
                contactAsJsonObj.put("ContactTitle", currContact.getContactTitle());
                contactAsJsonObj.put("Address", currContact.getAddress());
                contactAsJsonObj.put("City", currContact.getCity());
                contactAsJsonObj.put("Email", currContact.getEmail());
                contactAsJsonObj.put("PostalCode", currContact.getPostalCode());
                contactAsJsonObj.put("Country", currContact.getCountry());
                contactAsJsonObj.put("Phone", currContact.getPhone());
                contactAsJsonObj.put("Fax", currContact.getFax());

                addressBook.add(contactAsJsonObj);
            }

            return addressBook.toJSONString();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.out.println("Exception: "+e.getMessage());
            return null;
        }
    }

    public String jsonToXml(String jsonPath){
        try {
            JSONParser parser = new JSONParser();
            JSONArray addressBook = (JSONArray) parser.parse(new FileReader(jsonPath));

            List<Contact> contacts = new ArrayList<Contact>();
            for (Object contactAsObj : addressBook) {
                JSONObject contactAsJsonObj = (JSONObject) contactAsObj;
                Contact contact = new Contact();
                contact.setCustomerID(extractTextByAttributeName(contactAsJsonObj,"CustomerID"));
                contact.setCompanyName(extractTextByAttributeName(contactAsJsonObj,"CompanyName"));
                contact.setContactName(extractTextByAttributeName(contactAsJsonObj,"ContactName"));
                contact.setContactTitle(extractTextByAttributeName(contactAsJsonObj,"ContactTitle"));
                contact.setAddress(extractTextByAttributeName(contactAsJsonObj,"Address"));
                contact.setCity(extractTextByAttributeName(contactAsJsonObj,"City"));
                contact.setEmail(extractTextByAttributeName(contactAsJsonObj,"Email"));
                contact.setPostalCode(extractTextByAttributeName(contactAsJsonObj,"PostalCode"));
                contact.setCountry(extractTextByAttributeName(contactAsJsonObj,"Country"));
                contact.setPhone(extractTextByAttributeName(contactAsJsonObj,"Phone"));
                contact.setFax(extractTextByAttributeName(contactAsJsonObj,"Fax"));
                contacts.add(contact);
            }

            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("AddressBook");

            for (Contact contact : contacts) {
                Element contactElement = doc.createElement("Contact");
                addTextField(doc, contactElement, "CustomerID", contact.getCustomerID());
                addTextField(doc, contactElement, "CompanyName", contact.getCompanyName());
                addTextField(doc, contactElement, "ContactName", contact.getContactName());
                addTextField(doc, contactElement, "ContactTitle", contact.getContactTitle());
                addTextField(doc, contactElement, "Address", contact.getAddress());
                addTextField(doc, contactElement, "City", contact.getCity());
                addTextField(doc, contactElement, "Email", contact.getEmail());
                addTextField(doc, contactElement, "PostalCode", contact.getPostalCode());
                addTextField(doc, contactElement, "Country", contact.getCountry());
                addTextField(doc, contactElement, "Phone", contact.getPhone());
                addTextField(doc, contactElement, "Fax", contact.getFax());

                rootElement.appendChild(contactElement);
            }
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);

            String xmlString = result.getWriter().toString();

            return xmlString;
        } catch (IOException | ParseException | ParserConfigurationException | TransformerException e) {
            System.out.println("Exception: "+e.getMessage());
            return null;
        }
    }

    private String extractTextByTag(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);

        if (nodeList.getLength() == 0)
            return null;

        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private String extractTextByAttributeName(JSONObject jsonObject, String attrName) {
        Object attrValue = jsonObject.get(attrName);

        if (attrValue == null)
            return null;

        return attrValue.toString();
    }

    private void addTextField(Document doc, Element parentElement,
                                String fieldName, String fieldValue) {
        if (fieldValue == null)
            return;

        Element textElement = doc.createElement(fieldName);
        textElement.appendChild(doc.createTextNode(fieldValue));
        parentElement.appendChild(textElement);
    }
}
