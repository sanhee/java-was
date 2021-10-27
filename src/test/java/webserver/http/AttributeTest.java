package webserver.http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AttributeTest {

    @Test
    void createAttributeFromNoArgs() {
        Attribute attribute = new Attribute();
        assertThat(attribute.size()).isEqualTo(0);
    }

    @Test
    void createAttributeFromMap() {
        Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put("key", "value");

        Attribute attribute = Attribute.from(attributeMap);

        assertThat(attribute.size()).isEqualTo(1);
    }

    @Test
    void addAttribute() {
        Attribute attribute = new Attribute();
        attribute.add("key", "value");
        assertThat(attribute.size()).isEqualTo(1);
    }

    @Test
    void getAttribute() {
        Attribute attribute = new Attribute();
        attribute.add("key", "value");

        assertThat(attribute.get("key")).isEqualTo("value");
    }

    @Test
    void entrySet() {
        Attribute attribute = new Attribute();
        attribute.add("key", "value");
        attribute.add("key2", "value2");

        assertThat(attribute.entrySet().size()).isEqualTo(2);
    }

}