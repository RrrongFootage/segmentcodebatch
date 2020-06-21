package com.learnbatch.segmentcodebatch;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class TestServices {

  private static String maskXML(String xmlString, List<String> elementList) {
    String str = null;
    try {
      // Convert string to XML document
      Document document = toXmlDocument(xmlString);

      // Now mask the required fields in the XML
      maskElements(document.getDocumentElement(), elementList);

      // Convert document object to string
      str = toXmlString(document);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return str;
  }

  public static void main(String[] args) {

    String sqlPopiElement = getPopiElement().replaceAll("\\s+", "");
    System.out.println("sql result" + sqlPopiElement);
    String[] popiElement = sqlPopiElement.split(",");
    System.out.println("after splitting: " + popiElement.toString());
    List<String> elementsToMask = Arrays.asList(popiElement);

    for (String element : elementsToMask) {
      System.out.println("Element: " + element);
    }

    String inputXML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<VodaTest><password>test</password><idnohhh>9876543457689</idnohhh><pwd>abcdef</pwd><pword>800208075689</pword><secret>Java, Big data, Database</secret><idno>9008075631085</idno><idnumber>8366548987764</idnumber><identitynumber>8902836873</identitynumber><telephone>0791288294</telephone><cellnumber>028874634</cellnumber><telnumber>01193776584</telnumber>"
            + "<address>MartinPitjeng.com</address></VodaTest>";

    System.out.println("Input XML : \n" + inputXML);
    String maskedXML = maskXML(inputXML, elementsToMask);

    // Print the masked XML

    System.out.println("\nXML after masking : \n" + maskedXML);
  }

  private static Document toXmlDocument(String str)
      throws ParserConfigurationException, SAXException, IOException {

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

    Document document = docBuilder.parse(new InputSource(new StringReader(str)));

    return document;
  }

  private static String toXmlString(Document document) throws TransformerException {

    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    Transformer transformer = transformerFactory.newTransformer();

    DOMSource source = new DOMSource(document);

    StringWriter strWriter = new StringWriter();

    StreamResult result = new StreamResult(strWriter);

    transformer.transform(source, result);

    return strWriter.getBuffer().toString();
  }

  public static void maskElements(Node node, List<String> elemementList) {
    NodeList nodeList = node.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);
      // recursively call maskElements until you find a Leaf node
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        maskElements(currentNode, elemementList);
      } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
        // leaf node.. apply masking logic
        String name = currentNode.getParentNode().getNodeName();
        if (name != null && elemementList.contains(name)) {
          currentNode.setTextContent("MASKED_FOR_POPI");
        }
      }
    }
  }

  public static String getPopiElement() {

    String integrationType = "ActivateGSMservice";
    String serviceName = "MSPIActivateGSMServiceMediation";
    String integrationOperation = "activateGSMService";
    String direction = "Request";

    String sqlPopiElement = null;
    try {
      // Class.forName("com.mysql.cj.jdbc.Driver");
      Connection conn =
          DriverManager.getConnection("jdbc:mysql://localhost:3306/cc_citizen", "root", "1234");
      String sqlPOPI =
          "select POPI_Element from POPI where Integration_Type ='"
              + integrationType
              + "' AND ServiceName ='"
              + serviceName
              + "' AND Service_Operation='"
              + integrationOperation
              + "' AND Direction='"
              + direction
              + "'";
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sqlPOPI);

      while (rs.next()) {
        sqlPopiElement = rs.getString("POPI_ELEMENT");
        System.out.println(sqlPopiElement);
      }
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }

    return sqlPopiElement;
  }
}
