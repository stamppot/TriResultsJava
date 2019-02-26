package TriResultsJava;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLUtils {

    public static String getChildContent(Element parent, String name, String missing, String empty) {
        Element child = getChild(parent, name);
        if (child == null) {
            return missing;
        } else {
            String content = (String) getContent(child);
            return (content != null) ? content : empty;
        }
    }

    public static Object getContent(Element element) {
        NodeList nl = element.getChildNodes();
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    return node;
                case Node.CDATA_SECTION_NODE:
                case Node.TEXT_NODE:
                    content.append(node.getNodeValue());
                    break;
            }
        }
        return content.toString().trim();
    }

    public static Element getChild(Element parent, String name) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && name.equals(child.getNodeName())) {
                return (Element) child;
            }
        }
        return null;
    }

    public static List<String> extractTextChildren(Element parentNode, String childNode) {
//        Node parent = parentNode;
//        if(childNode != null) {
//            parent = getChild(parentNode, childNode);
//        }
        NodeList childNodes = parentNode.getChildNodes();
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
//            if (node.getNodeType() == Node.TEXT_NODE) {
//                result.add(node.getTextContent());
//            }
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == childNode) {
                result.add(node.getTextContent());
            }
        }
        return result;
    }
}