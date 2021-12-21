package webserver.http.message;

import webserver.Const;
import webserver.http.Body;
import webserver.http.header.ResponseHeader;
import webserver.http.startline.StatusLine;

import java.util.StringJoiner;

import static webserver.http.header.Header.parseStartLine;

public class ResponseMessage {

    private StatusLine statusLine;
    private ResponseHeader header;
    private Body body;

    public ResponseMessage(StatusLine statusLine, ResponseHeader header, Body body) {
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage from(String responseMessage) {
        String[] splittedResponseMessage = responseMessage.split(Const.CRLF + Const.CRLF);
        StatusLine statusLine = StatusLine.from(parseStartLine(splittedResponseMessage[0]));

        StringJoiner body = new StringJoiner(Const.CRLF + Const.CRLF);

        for (int i = 1; i < splittedResponseMessage.length; i++) {
            body.add(splittedResponseMessage[i]);
        }

        return new ResponseMessage(statusLine, ResponseHeader.from(splittedResponseMessage[0]), Body.from(body.toString()));
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}
