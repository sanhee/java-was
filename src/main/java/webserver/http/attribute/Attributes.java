package webserver.http.attribute;

import util.HttpRequestUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class Attributes {
    private final Map<String, String> attributes = new LinkedHashMap<>();

    public static Attributes from(Map<String, String> attributes) {
        return new Attributes().addAll(attributes);
    }

    public static Attributes from(String headerText) {
        Map<String, String> attributes = new LinkedHashMap<>();

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.put(pair.getKey(), pair.getValue());
            }
        }

        return from(attributes);
    }

    public Attributes add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public Attributes addAll(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public String get(String key) {
        return attributes.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    @Override
    public String toString() {
        StringJoiner attributesToString = new StringJoiner("\r\n");
        for (Map.Entry<String, String> each : attributes.entrySet()) {
            attributesToString.add(each.getKey() + ": " + each.getValue());
        }

        return attributesToString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attributes that = (Attributes) o;
        return Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }

}
