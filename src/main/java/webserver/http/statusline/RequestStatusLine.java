package webserver.http.statusline;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class RequestStatusLine extends StatusLine {

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

    public String method() {
        return statusLineAttributeBy(METHOD_KEY);
    }

    private URI uri() {
        try {
            return new URI(statusLineAttributeBy(PATH_KEY));
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Request의 Path가 올바르지 않음. path : " + path(), e);
        }
    }

    public String path() {
        return uri().getPath();
    }

    public String queryString() {
        return Objects.toString(uri().getQuery(), "");
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(method())
                                    .add(path())
                                    .add(protocol())
                                    .toString();
    }

    public String pathExtension() {
        String path = path();

        String[] splitPath = path.split("/");

        if (splitPath.length == 0) {
            return "";
        }

        String[] splitDot = splitPath[splitPath.length - 1].split("\\.");

        String extension = "";

        if (1 < splitDot.length) {
            extension = splitPath[splitPath.length - 1].split("\\.")[1];
        }

        return extension;
    }
}
