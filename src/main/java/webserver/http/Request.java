package webserver.http;

import webserver.http.message.RequestMessage;

public class Request {
    private RequestMessage requestMessage;

    protected Request(RequestMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public static Request from(String message) {
        return new Request(RequestMessage.from(message));
    }

    public String path() {
        return requestMessage.getHeader().path();
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }
}
