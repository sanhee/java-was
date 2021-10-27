package webserver.http.attribute;

import java.util.Map;

public class Attributes {
    private Map<String, String> attributes;

    public Attributes add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public String get(String key) {
        return attributes.get(key);
    }
}
