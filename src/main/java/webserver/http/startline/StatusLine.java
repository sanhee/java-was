package webserver.http.startline;

import webserver.http.attribute.Attributes;

import java.util.List;
import java.util.StringJoiner;

public class StatusLine extends StartLine {
    private static final String STATUS_CODE_KEY = "statusCode";
    private static final String STATUS_TEXT_KEY = "statusText";


    public StatusLine(Attributes statusLineAttributes) {
        super(statusLineAttributes);
    }

    public static StatusLine from(List<String> statusLine) {
        Attributes statusLineAttributes = new Attributes();

        statusLineAttributes.add(PROTOCOL_VERSION_KEY, statusLine.get(0));
        statusLineAttributes.add(STATUS_CODE_KEY, statusLine.get(1));
        statusLineAttributes.add(STATUS_TEXT_KEY, statusLine.get(2));

        return new StatusLine(statusLineAttributes);
    }

    public String getStatusCode() {
        return getAttributeBy(STATUS_CODE_KEY);
    }

    public String getStatusText() {
        return getAttributeBy(STATUS_TEXT_KEY);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(getProtocol())
                                    .add(getStatusCode())
                                    .add(getStatusText())
                                    .toString();
    }
}
