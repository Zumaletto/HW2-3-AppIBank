package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private UserGenerator() {
    }

    @Value
    public static class AuthUser {
        String login;
        String password;
        String status;
    }

    private static void sendRequest(AuthUser user) {
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .body(user) // передаём в теле объект, который будет преобразован в JSON
                        .when() // "когда"
                        .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200); // код 200 OK
                    }

    public static AuthUser getValidActiveUser(){
        AuthUser activeUser = new AuthUser(
                faker.name().username(),
                faker.internet().password(),
                "active");
        sendRequest(activeUser);
        return activeUser;
    }

    public static AuthUser getValidBlockedUser(){
        AuthUser blockedUser = new AuthUser(
                faker.name().username(),
                faker.internet().password(),
                "blocked");
        sendRequest(blockedUser);
        return blockedUser;
    }

    public static AuthUser getInvalidPassword(){
        String login = faker.name().username().toLowerCase();
        AuthUser invalidPass = new AuthUser(
                login,
                "validPassword",
                "active");
        sendRequest(invalidPass);
        return new AuthUser(login, "invalidPassword", "active");
    }

    public static AuthUser getInvalidLogin(){
        String password = faker.internet().password();
        AuthUser invalidLog = new AuthUser(
                "login",
                password,
                "active");
        sendRequest(invalidLog);
        return new AuthUser("invalidLogin", password, "active");
    }

}
