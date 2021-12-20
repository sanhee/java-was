package webserver.http.message;

import util.HttpRequestUtils;
import webserver.http.Body;
import webserver.http.header.RequestHeader;
import webserver.http.startline.RequestLine;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PostMessage implements RequestMessage {
    private RequestLine requestLine;
    private RequestHeader header;
    private Body body;

    public PostMessage(RequestLine requestLine, RequestHeader header, Body body) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    @Override
    public RequestHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String getMethod() {
        return requestLine.getMethod();
    }

    @Override
    public Map<String, String> getParameters() {
        try {
            String decode = URLDecoder.decode(body.getString(), StandardCharsets.UTF_8.name());
            return HttpRequestUtils.parseQueryString(decode);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("인코딩 오류", e);
        }
    }

    @Override
    public String getPath() {
        return requestLine.getPath();
    }
}
