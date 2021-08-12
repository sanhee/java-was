package webserver.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BodyTest {

    @Test
    void init() {
        String data = "Hello World";
        Body expectedBody = Body.from(data);

        Body body = Body.from(data);

        assertThat(body).isEqualTo(expectedBody);
    }
}
