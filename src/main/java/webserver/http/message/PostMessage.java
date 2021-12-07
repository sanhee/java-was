package webserver.http.message;

import util.HttpRequestUtils;
import webserver.Const;
import webserver.http.Body;
import webserver.http.header.RequestHeader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PostMessage implements RequestMessage {
    private RequestHeader header;
    private Body body;

    public PostMessage(RequestHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static PostMessage from(String postMessage) {
        String[] splittedPostMessage = postMessage.split(Const.CRLF + Const.CRLF);

        Body body = Body.from(splittedPostMessage.length != 1 ? splittedPostMessage[1] : "");

        return new PostMessage(RequestHeader.from(splittedPostMessage[0]), body);
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
        return header.getMethod();
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
}
