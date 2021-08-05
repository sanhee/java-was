package model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserTest {

    @ParameterizedTest
    @MethodSource
    void checkPassword(User user, String password) {

        user.checkPassword(password);
    }

    static Stream<Arguments> checkPassword() {
        return Stream.of(
                Arguments.arguments(
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "test"
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkPasswordWithFailed(User user, String password) {

        assertThatExceptionOfType(PasswordNotMatchException.class)
                .isThrownBy(() -> user.checkPassword(password));
    }

    static Stream<Arguments> checkPasswordWithFailed() {
        return Stream.of(
                Arguments.arguments(
                        User.builder()
                                .setUserId("test")
                                .setPassword("test")
                                .setName("test")
                                .setEmail("test@test")
                                .build(),
                        "test2"
                )
        );
    }
}
