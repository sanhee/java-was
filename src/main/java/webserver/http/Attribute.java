package webserver.http;

import util.HttpRequestUtils;

import java.util.*;

public class Attribute {
    private Map<String, String> attributes;

    public Attribute() {
        attributes = new HashMap<>();
    }

    private Attribute(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public static Attribute from(Map<String, String> attributeMap) {
        return new Attribute(attributeMap);
    }

    public void add(String key, String value) {
        attributes.put(key, value);
    }

    public int size() {
        return attributes.size();
    }

    public String get(String key) {
        return attributes.getOrDefault(key, "empty");
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return attributes.entrySet();
    }

    public static Attribute attributeFrom(String headerText) {
        Map<String, String> attributes = new LinkedHashMap<>();

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.put(pair.getKey(), pair.getValue());
            }
        }

        return Attribute.from(attributes);
    }

    public String getOrDefault(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(attributes, attribute.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }
}
