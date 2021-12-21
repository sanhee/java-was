package webserver.http;

import webserver.Const;
import webserver.http.message.ResponseMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private ResponseMessage responseMessage;

    public Response(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static Response from(String message) {
        return new Response(ResponseMessage.from(message));
    }

    public void write(OutputStream outputStream) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        // TODO: message.getBytes() 와 같이 수정 가능
        byte[] status = (responseMessage.getStatusLine().toString() + Const.CRLF).getBytes(StandardCharsets.UTF_8);
        byte[] header = responseMessage.getHeader().getBytes();
        byte[] body = responseMessage.getBody().getBytes();

        dos.write(status);
        dos.write(header);
        dos.write(body);
        dos.flush();
    }
}
