package webserver.http.attribute;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class AttributesTest {

    @Test
    void from() {
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.add("key", "value");

        Attributes actualAttributes = Attributes.from(new HashMap() {{
            put("key", "value");
        }});

        assertThat(actualAttributes).isEqualTo(expectedAttributes);
    }

    @Test
    void add() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");
    }

    @Test
    void get() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        assertThat(attributes.get("key")).isEqualTo("value");
    }

    @Test
    void getBytes() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        assertThat(attributes.getBytes()).isEqualTo("key: value".getBytes());

    }
}
