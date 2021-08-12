package webserver.http;

import webserver.http.message.ResponseMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
        byte[] header = responseMessage.getHeader().getBytes();
        byte[] body = responseMessage.getBody().getBytes();

        dos.write(header);
        dos.write(body);
        dos.flush();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(responseMessage, response.responseMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseMessage);
    }
}
