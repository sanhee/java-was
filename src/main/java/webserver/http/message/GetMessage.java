package webserver.http.message;

import util.HttpRequestUtils;
import webserver.http.header.RequestHeader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

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
        String path = header.path();

        try {
            URI uri = new URI(path);
            return HttpRequestUtils.parseQueryString(uri.getQuery());

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Request의 Path가 올바르지 않음. path : " + path, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetMessage that = (GetMessage) o;
        return Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }
}
