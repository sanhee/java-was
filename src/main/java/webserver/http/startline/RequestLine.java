package webserver.http.startline;

import webserver.http.attribute.Attributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class RequestLine extends StartLine {

    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";

    public RequestLine(Attributes statusLineAttributes) {
        super(statusLineAttributes);
    }

    public static RequestLine from(List<String> statusLine) {
        Attributes statusLineAttributes = new Attributes();

        statusLineAttributes.add(METHOD_KEY, statusLine.get(0));
        statusLineAttributes.add(PATH_KEY, statusLine.get(1));
        statusLineAttributes.add(PROTOCOL_VERSION_KEY, statusLine.get(2));

        return new RequestLine(statusLineAttributes);
    }

    public String getMethod() {
        return getAttributeBy(METHOD_KEY);
    }

    private URI getUri() {
        try {
            return new URI(getAttributeBy(PATH_KEY));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Request의 Path가 올바르지 않음. path : " + getAttributeBy(PATH_KEY), e);
        }
    }

    public String getPath() {
        return getUri().getPath();
    }

    public String getQueryString() {
        return Objects.toString(getUri().getQuery(), "");
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(getMethod())
                                    .add(getPath())
                                    .add(getProtocol())
                                    .toString();
    }
}
