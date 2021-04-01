package webserver;

public class Request {
    private RequestMessage requestMessage;

    private Request(RequestMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public static Request from(String message) {
        return new Request(RequestMessage.from(message));
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }
}
