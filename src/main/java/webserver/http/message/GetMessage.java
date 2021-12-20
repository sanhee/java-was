package webserver.http.message;

import util.HttpRequestUtils;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.util.Map;

public class GetMessage implements RequestMessage {
    private RequestLine requestLine;
    private RequestHeader header;

    public GetMessage(RequestLine requestLine, RequestHeader header) {
        this.requestLine = requestLine;
        this.header = header;
    }

    @Override
    public RequestHeader getHeader() {
        return header;
    }

    @Override
    public String getMethod() {
        return requestLine.getMethod();
    }

    @Override
    public Map<String, String> getParameters() {
        return HttpRequestUtils.parseQueryString(getQueryString());
    }

    @Override
    public String getPath() {
        return requestLine.getPath();
    }

    @Override
    public RequestLine getRequestLine() {
        return requestLine;
    }

    public String getQueryString() {
        return requestLine.getQueryString();
    }
}
