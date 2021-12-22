package webserver.http.attribute;

import org.junit.jupiter.api.AfterEach;
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


    @ParameterizedTest
    @DisplayName("add 테스트: key의 대소문자 관계 없이 Attributes를 꺼내올 수 있음")
    @MethodSource("add")
    void add(String expectedValue, String actualValue) {
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    static Stream<Arguments> add() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        return Stream.of(
                Arguments.of(
                        "value",
                        attributes.get("KEY")
                ),
                Arguments.of(
                        "value",
                        attributes.get("key")
                ),
                Arguments.of(
                        "value",
                        attributes.get("kEy")
                )
        );
    }

    @ParameterizedTest
    @DisplayName("addAll 테스트: key의 대소문자 관계 없이 Attributes를 꺼내올 수 있음")
    @MethodSource("addAll")
    void addAll(String expectedValue, String actualValue) {
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    static Stream<Arguments> addAll() {
        Attributes attributes = new Attributes();
        Map<String, String> attributeMap = new LinkedHashMap<>();
        attributeMap.put("key", "value");
        attributes.addAll(attributeMap);

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.add("key", "value");

        return Stream.of(
                Arguments.of(
                        expectedAttributes.get("key"),
                        attributes.get("KEY")
                ),
                Arguments.of(
                        expectedAttributes.get("KEY"),
                        attributes.get("kEY")
                ),
                Arguments.of(
                        expectedAttributes.get("kEy"),
                        attributes.get("kEy")
                )
        );
    }

    @Test
    void get() {
        Attributes attributes = new Attributes();
        attributes.add("key", "value");

        assertThat(attributes.get("key")).isEqualTo("value");
    }

    @Test
    @DisplayName("존재하지 않는 키를 조회하면 null")
    void getValueWithEmptyKey() {
        Attributes attributes = new Attributes();
        assertThat(attributes.get("null")).isNull();
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

    @Test
    @DisplayName("value가 null인 key")
    void isNullWhenNullValue(){
        Attributes attributes = new Attributes();
        attributes.add("null", null);
        attributes.add("null2", null);

        assertAll(
                ()-> assertThat(attributes.size()).isEqualTo(2),
                ()-> assertThat(attributes.get("null")).isNull(),
                ()-> assertThat(attributes.get("null2")).isNull()
        );

    }
}
