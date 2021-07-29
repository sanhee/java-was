package model;

public class UserBuilder {
    private String userId;
    private String password;
    private String name;
    private String email;

    private UserBuilder() {
    }

    static UserBuilder create() {
        return new UserBuilder();
    }

    public UserBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public User build() {
        return User.of(userId, password, name, email);
    }
}
