package webserver.http.statusline;

import webserver.http.header.RequestHeader;

import java.util.Map;

public class RequestStatusLine extends StatusLine {
    public RequestStatusLine(Map<String, String> statusLineAttributes) {
        super(statusLineAttributes);
    }

    public String method() {
        return statusLineAttributeBy(RequestHeader.METHOD_KEY);
    }

    public String path() {
        return statusLineAttributeBy(RequestHeader.PATH_KEY)
                .split("\\?")[0];
    }
}
