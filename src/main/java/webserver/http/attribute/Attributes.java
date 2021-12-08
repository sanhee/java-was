package webserver.http.attribute;

import util.HttpRequestUtils;
import webserver.Const;

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
        Attributes attributes = new Attributes();

        String[] splittedHeaderTexts = headerText.split(Const.CRLF);
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.add(pair.getKey(), pair.getValue());
            }
        }

        return attributes;
    }

    public Attributes add(String key, String value) {
        for (String k : attributes.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return this;
            }
        }

        attributes.put(key, value);
        return this;
    }

    public Attributes addAll(Map<String, String> attributes) {
        for (String key : attributes.keySet()) {
            add(key, attributes.get(key));
        }

        return this;
    }

    public String get(String key) {
        for (String k : attributes.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return attributes.get(k);
            }
        }
        return null;
    }

    public String getOrDefault(String key, String defaultValue) {
        for (String k : attributes.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return attributes.get(k);
            }
        }
        return defaultValue;
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
