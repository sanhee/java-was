package webserver.http.attribute;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("add 테스트: key의 대소문자 관계 없이 Attributes를 꺼내올 수 있음")
    void add() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        assertAll(
                () -> assertEquals("value", attributes.get("KEY")),
                () -> assertEquals("value", attributes.get("key")),
                () -> assertEquals("value", attributes.get("kEy"))
        );
    }

    @Test
    @DisplayName("addAll 테스트: key의 대소문자 관계 없이 Attributes를 꺼내올 수 있음")
    void addAll() {
        Attributes attributes = new Attributes();
        Map<String, String> attributeMap = new LinkedHashMap<>();
        attributeMap.put("key", "value");
        attributes.addAll(attributeMap);

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.add("key", "value");

        assertAll(
                () -> assertEquals(expectedAttributes.get("key"), attributes.get("KEY")),
                () -> assertEquals(expectedAttributes.get("KEY"), attributes.get("kEY")),
                () -> assertEquals(expectedAttributes.get("kEy"), attributes.get("key"))
        );
    }

    @Test
    void get() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        assertThat(attributes.get("key")).isEqualTo("value");
    }

    @Test
    @DisplayName("존재하지 않는 키를 조회하면 예외발생")
    void getValueWithEmptyKey() {
        Attributes attributes = new Attributes();
        assertThatIllegalArgumentException().isThrownBy(() -> attributes.get("emptyKey"));
    }

    @ParameterizedTest
    @MethodSource("getOrDefault")
    void getOrDefault(Attributes attributes, String defaultValue, String key, String expectedValue) {
        String actualValue = attributes.getOrDefault(key, defaultValue);
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("존재하지 않는 키를 조회하면 주어진 기본값으로 대체")
    void getDefaultValueWithEmptyKey() {
        Attributes attributes = new Attributes();
        assertThat(attributes.getOrDefault("emptyKey", "happy")).isEqualTo("happy");
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
    void toHeaderText() {
        Attributes attributes = new Attributes();
        attributes.add("key1", "value1");
        attributes.add("key2", "value2");

        assertThat(attributes.toHeaderText()).isEqualTo("key1: value1\r\nkey2: value2");

    }
}
