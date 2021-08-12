package webserver.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Body {
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    private byte[] data;

    private Body(byte[] data) {
        this.data = data;
    }

    public static Body from(String bodyText) {
        return new Body(bodyText.getBytes(DEFAULT_ENCODING));
    }

    public String getString() {
        return new String(data, DEFAULT_ENCODING);
    }

    public byte[] getBytes() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body = (Body) o;
        return Arrays.equals(data, body.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
        return "Body{" +
                "data=" + new String(data) +
                '}';
    }
}
