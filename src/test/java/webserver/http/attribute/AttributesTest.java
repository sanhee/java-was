package webserver.http.attribute;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

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
    void fromHeaderText() {
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.add("key1", "value1");
        expectedAttributes.add("key2", "value2");

        String headerText = "key1: value1\r\nkey2: value2";
        //TODO: Header 명세 구현 시 key2:value2 와 같은 경우 확인 필요
        Attributes actualAttributes = Attributes.from(headerText);

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

    @ParameterizedTest
    @MethodSource("getOrDefault")
    void getOrDefault(Attributes attributes, String defaultValue, String key, String expectedValue) {
        String actualValue = attributes.getOrDefault(key, defaultValue);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    static Stream<Arguments> getOrDefault() {
        return Stream.of(
                Arguments.of(
                        new Attributes().add("key", "value"),
                        "",
                        "key",
                        "value"
                ),
                Arguments.of(
                        new Attributes(),
                        "0",
                        "key",
                        "0"
                )
        );
    }

    @Test
    void toStringTest() {
        Attributes attributes = new Attributes();
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        assertThat(attributes.toString()).isEqualTo("key1: value1\r\nkey2: value2");

    }
}
