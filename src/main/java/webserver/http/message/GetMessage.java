package webserver.http.message;

import util.HttpRequestUtils;
import webserver.http.header.RequestHeader;

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
        return HttpRequestUtils.parseQueryString(header.queryString());
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
