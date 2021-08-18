package webserver.http.message;

import util.HttpRequestUtils;
import webserver.http.header.RequestHeader;

import java.util.Map;

public class GetMessage implements RequestMessage {
    private RequestHeader header;

    public GetMessage(RequestHeader header) {
        this.header = header;
    }

    public static GetMessage from(String getMessage) {
        return new GetMessage(RequestHeader.from(getMessage));
    }

    @Override
    public RequestHeader getHeader() {
        return header;
    }

    @Override
    public String getMethod() {
        return header.getMethod();
    }

    @Override
    public Map<String, String> getParameters() {
        return HttpRequestUtils.parseQueryString(header.getQueryString());
    }
}
