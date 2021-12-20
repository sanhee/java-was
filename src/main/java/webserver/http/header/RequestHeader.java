package webserver.http.header;

import webserver.http.attribute.Attributes;

public class RequestHeader extends Header {

    protected RequestHeader(Attributes attributes) {
        super(attributes);
    }

    public static RequestHeader of(Attributes attributes) {
        return new RequestHeader(attributes);
    }

    public static RequestHeader from(String headerText) {
        return RequestHeader.of(Attributes.from(headerText));
    }
}
