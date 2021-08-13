package webserver.http.statusline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    public String path() {
        return statusLineAttributeBy(PATH_KEY)
                .split("\\?")[0];
    }

    public String queryString() {
        String[] splitPath = statusLineAttributeBy(PATH_KEY)
                .split("\\?");

        return splitPath.length == 2 ? splitPath[1] : "";
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(method())
                                    .add(path())
                                    .add(protocol())
                                    .toString();
    }
}
