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
        attributes.put(key.toUpperCase(), value);
        return this;
    }

    public Attributes addAll(Map<String, String> attributes) {
        Map<String, String> upperAttributes = new LinkedHashMap<>();

        for (String key : attributes.keySet()) {
            upperAttributes.put(key.toUpperCase(), attributes.get(key));
        }

        this.attributes.putAll(upperAttributes);
        return this;
    }

    public String get(String key) {
        return attributes.get(key.toUpperCase());
    }

    public String getOrDefault(String key, String defaultValue) {
        return attributes.getOrDefault(key.toUpperCase(), defaultValue);
    }

    public String toHeaderText() {
        StringJoiner headerText = new StringJoiner("\r\n");
        for (Map.Entry<String, String> each : attributes.entrySet()) {
            headerText.add(each.getKey() + ": " + each.getValue());
        }

        return headerText.toString();
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
