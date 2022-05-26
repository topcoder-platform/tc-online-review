package com.cronos.onlinereview.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private Map<String, Property> propertyMap;

    public ConfigManager() {
        propertyMap = new LinkedHashMap<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(ConfigManager.class.getClassLoader().getResourceAsStream("config.xml"));
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodes.item(i);
                    String name = element.getAttribute("name");
                    propertyMap.put(name, parseProperty(element));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        return new ConfigManager();
    }

    private static Property parseProperty(Element element) {
        Property property = new Property();
        property.children = new LinkedHashMap<>();
        property.value = new ArrayList<>();
        List<Element> values = filterNodes(element.getChildNodes(), "Value");
        for (Element e : values) {
            property.value.add(e.getFirstChild().getNodeValue());
        }
        List<Element> properties = filterNodes(element.getChildNodes(), "Property");
        for (Element e : properties) {
            String name = e.getAttribute("name");
            property.children.put(name, parseProperty(e));
        }
        return property;
    }

    private static List<Element> filterNodes(NodeList nl, String tagName) {
        List<Element> result = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE && ((Element) nl.item(i)).getTagName().equals(tagName)) {
                result.add((Element) nl.item(i));
            }
        }
        return result;
    }

    public String getString(String p1, String p2) {
        return propertyMap.get(p2).getValue();
    }

    public Property getPropertyObject(String p1, String p2) {
        return propertyMap.get(p2);
    }

    public Property getProperty(String p1, String p2) {
        return propertyMap.get(p2);
    }

    public static class Property {
        private LinkedHashMap<String, Property> children;
        private List<String> value;

        public Enumeration propertyNames() {
            return Collections.enumeration(children.keySet());
        }

        public String getValue(String key) {
            String[] keys = key.split("\\.");
            Property p = this;
            for (String k : keys) {
                p = p.children.get(k);
                if (p == null) {
                    return null;
                }
            }
            return p.getValue();
        }

        public String getValue() {
            if (value.isEmpty()) {
                return null;
            }
            return value.get(0);
        }

        public String[] getValues(String key) {
            String[] keys = key.split("\\.");
            Property p = this;
            for (String k : keys) {
                p = p.children.get(k);
                if (p == null) {
                    return new String[0];
                }
            }
            return p.getValues();
        }

        public String[] getValues() {
            return value.stream().toArray(String[]::new);
        }

        public boolean containsProperty(String key) {
            return children.containsKey(key);
        }
    }
}
