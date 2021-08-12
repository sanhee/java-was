package util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IOUtilsTest {

    @ParameterizedTest
    @MethodSource
    void readData(String data, String expectedData) throws Exception {
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        assertThat(IOUtils.readData(br, data.length())).isEqualTo(expectedData);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> readData() {
        return Stream.of(
                Arguments.of("abcd123", "abcd123")
        );
    }
}
