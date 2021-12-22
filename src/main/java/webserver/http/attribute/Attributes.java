package webserver.http.attribute;

import util.HttpRequestUtils;
import webserver.Const;

import java.util.*;

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

    public Attributes add(String targetKey, String value) {
        for (String currentKey : attributes.keySet()) {
            if (currentKey.equalsIgnoreCase(targetKey)) {
                return this;
            }
        }

        attributes.put(targetKey, value);
        return this;
    }

    public Attributes addAll(Map<String, String> attributes) {
        for (String currentKey : attributes.keySet()) {
            add(currentKey, attributes.get(currentKey));
        }

        return this;
    }

    public String get(String targetKey) {
        return getOrDefault(targetKey, null);
    }

    public String getOrDefault(String targetKey, String defaultValue) {
        String findValue = null;

        for (String currentKey : attributes.keySet()) {
            if (currentKey.equalsIgnoreCase(targetKey)) {
                findValue = attributes.get(currentKey);
            }
        }

        return findValue != null ? findValue : defaultValue;
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
