package webserver.http;

import webserver.http.message.ResponseMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
        // byte[] status = responseMessage.get
        byte[] header = responseMessage.getHeader().getBytes();
        byte[] body = responseMessage.getBody().getBytes();

        dos.write(header);
        dos.write(body);
        dos.flush();
    }
}
