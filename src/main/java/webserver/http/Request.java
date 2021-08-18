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

    public String getPath() {
        return requestMessage.getPath();
    }

    public RequestMessage getRequestMessage() {
        return requestMessage;
    }

    public String getPathExtension() {
        String path = getPath();

        String[] splitPath = path.split("/");

        if (splitPath.length == 0) {
            return "";
        }

        String[] splitDot = splitPath[splitPath.length - 1].split("\\.");

        String extension = "";

        if (1 < splitDot.length) {
            extension = splitPath[splitPath.length - 1].split("\\.")[1];
        }

        return extension;
    }
}
