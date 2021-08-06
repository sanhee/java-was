package db;

import model.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DataBaseTest {

    @ParameterizedTest
    @MethodSource("deleteAll")
    void deleteAll(User user) {
        DataBase.addUser(user);
        DataBase.deleteAll();
        assertThat(DataBase.findAll()).isEmpty();
    }

    static Stream<Arguments> deleteAll() {
        return Stream.of(
                Arguments.arguments(
                        User.builder()
                                .setName("n")
                                .setUserId("u")
                                .setEmail("e")
                                .setPassword("p")
                                .build()
                ));
    }

}