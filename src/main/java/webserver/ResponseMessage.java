package webserver;

import java.util.Objects;
import java.util.StringJoiner;

public class ResponseMessage {
    private ResponseHeader header;
    private Body body;

    public ResponseMessage(ResponseHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage from(String responseMessage) {
        String[] splittedResponseMessage = responseMessage.split(System.lineSeparator() + System.lineSeparator());

        StringJoiner body = new StringJoiner(System.lineSeparator() + System.lineSeparator());

        for (int i = 1; i < splittedResponseMessage.length; i++) {
            body.add(splittedResponseMessage[i]);
        }

        return new ResponseMessage(Header.responseHeaderFrom(splittedResponseMessage[0]), Body.from(body.toString()));
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseMessage that = (ResponseMessage) o;
        return Objects.equals(header, that.header) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }
}
