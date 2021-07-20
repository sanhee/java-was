package model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class UserTest {

    @ParameterizedTest
    @MethodSource("checkPassword")
    void checkPassword(User user, String password) {
        user.checkPassword(password);
    }

    static Stream<Arguments> checkPassword() {
        return Stream.of(
                Arguments.arguments(
                        new User(
                                "dae",
                                "dae",
                                "dae",
                                "dae@dae.com"
                        ),
                        "dae"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("checkPasswordThrowsPasswordNotMatchException")
    void checkPasswordThrowsPasswordNotMatchException(User user, String password) {
        Assertions.assertThatExceptionOfType(PasswordNotMatchException.class)
                .isThrownBy(()->user.checkPassword(password));
    }

    static Stream<Arguments> checkPasswordThrowsPasswordNotMatchException() {
        return Stream.of(
                Arguments.arguments(
                        new User(
                                "dae",
                                "dae",
                                "dae",
                                "dae@dae.com"
                        ),
                        "dae2"
                )
        );
    }
}
