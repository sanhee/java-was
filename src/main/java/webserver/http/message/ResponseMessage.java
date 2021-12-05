package webserver.http.message;

import webserver.Const;
import webserver.http.Body;
import webserver.http.header.ResponseHeader;

import java.util.StringJoiner;

public class ResponseMessage {
    private ResponseHeader header;
    private Body body;

    public ResponseMessage(ResponseHeader header, Body body) {
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage from(String responseMessage) {
        String[] splittedResponseMessage = responseMessage.split(Const.CRLF + Const.CRLF);

        StringJoiner body = new StringJoiner(Const.CRLF + Const.CRLF);

        for (int i = 1; i < splittedResponseMessage.length; i++) {
            body.add(splittedResponseMessage[i]);
        }

        return new ResponseMessage(ResponseHeader.from(splittedResponseMessage[0]), Body.from(body.toString()));
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}
