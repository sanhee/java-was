package webserver.http.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class Attributes {
    private Map<String, String> attributes;

    public Attributes() {
        this(new HashMap<>());
    }

    private Attributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public static Attributes from(Map<String, String> attributes) {
        return new Attributes(attributes);
    }

    public Attributes add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public String get(String key) {
        return attributes.get(key);
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
