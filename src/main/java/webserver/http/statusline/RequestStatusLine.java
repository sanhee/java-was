package webserver.http.statusline;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class RequestStatusLine extends StartLine {

    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";

    public RequestStatusLine(Map<String, String> statusLineAttributes) {
        super(statusLineAttributes);
    }

    public static RequestStatusLine from(List<String> statusLine) {
        Map<String, String> statusLineAttributes = new HashMap<>();

        statusLineAttributes.put(METHOD_KEY, statusLine.get(0));
        statusLineAttributes.put(PATH_KEY, statusLine.get(1));
        statusLineAttributes.put(PROTOCOL_VERSION_KEY, statusLine.get(2));

        return new RequestStatusLine(statusLineAttributes);
    }

    public String getMethod() {
        return getStatusLineAttributeBy(METHOD_KEY);
    }

    private URI getUri() {
        try {
            return new URI(getStatusLineAttributeBy(PATH_KEY));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Request의 Path가 올바르지 않음. path : " + getPath(), e);
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
