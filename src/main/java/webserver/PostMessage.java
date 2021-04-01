package webserver;

import util.HttpRequestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class PostMessage implements RequestMessage {
    private RequestHeader header;
    private Body body;

    public PostMessage(RequestHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static PostMessage from(String postMessage) {
        String[] splittedPostMessage = postMessage.split(System.lineSeparator() + System.lineSeparator());
        //TODO body가 비어서 들어오는 경우 실제 form으로 테스트 해서 확인해볼 필요 있음
        return new PostMessage(Header.requestHeaderFrom(splittedPostMessage[0]), Body.from(splittedPostMessage[1]));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostMessage that = (PostMessage) o;
        return Objects.equals(header, that.header) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }

    @Override
    public String toString() {
        return "PostMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
