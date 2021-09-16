package webserver.http.header;

import util.HttpRequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Header {
    private Map<String, String> attributes;

    protected Header(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    protected static Map<String, String> attributeFrom(String headerText) {
        Map<String, String> attributes = new LinkedHashMap<>();

        String[] splittedHeaderTexts = headerText.split(System.lineSeparator());
        for (String splittedHeaderText : splittedHeaderTexts) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(splittedHeaderText);

            if (pair != null) {
                attributes.put(pair.getKey(), pair.getValue());
            }
        }

        return attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();

        sb.append(getStatusLine()).append(System.lineSeparator());

        for (Map.Entry<String, String> entry : getAttributes().entrySet()) {
            sb.append(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    protected abstract String getStatusLine();

    public int getContentLength() {
        /*
         * TODO:Transfer-Encoding이 포함되면 Content-Length가 포함되지 않아야 한다고 함. 찾아보기
         *  A sender MUST NOT send a Content-Length header field in any message that contains a Transfer-Encoding header field.
         *  https://datatracker.ietf.org/doc/html/rfc7230#section-3.3.2
         *  만약 이 경우 body 읽는 로직 바뀌어야 할 수 있음(현재는 Content-length에 따라서 읽도록 되어있음)
         */
        return Integer.parseInt(attributes.getOrDefault("Content-Length", "0"));
    }
}
