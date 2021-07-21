package webserver;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super();
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }
}
