package tests.registration;

import models.RegistrationBodyModel;
import models.RegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static specs.RegistrationSpec.*;

@Tag("smoke")
@DisplayName("Проверяет API метод для регистрации пользователя")
public class CheckRegistrationTests extends TestBase {

    private static final String EMAIL = "eve.holt@reqres.in";
    private static final String PASSWORD = "pistol";
    private static final String ERROR_MESSAGE_PASSWORD = "Missing password";
    private static final String ERROR_MESSAGE_EMAIL = "Missing email or username";
    private static final String API_PATH = "/register";

    @Test
    @DisplayName("Проверяет успешную регистрацию пользователя")
    void checkSuccessfulRegistrationTest() {
        RegistrationBodyModel registration = new RegistrationBodyModel();
        registration.setEmail(EMAIL);
        registration.setPassword(PASSWORD);

        RegistrationResponseModel response = step("Выполняем запрос регистрации пользователя", () ->
                given(registrationRequestSpec)
                        .body(registration)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(registrationResponseSpec)
                        .extract().as(RegistrationResponseModel.class)
        );

        step("Проверяем получение токена в теле ответа после успешной регистрации", () ->
                assertThat("Не получили токен в теле ответа", response.getToken(), notNullValue())
        );
    }

    @Test
    @DisplayName("Проверяет ошибку регистрации при отсутствии ввода пароля")
    void checkRegistrationWithoutPasswordTest() {
        RegistrationBodyModel registration = new RegistrationBodyModel();
        registration.setEmail(EMAIL);

        RegistrationResponseModel response = step("Выполняем запрос регистрации пользователя", () ->
                given(registrationRequestSpec)
                        .body(registration)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(missingPasswordOrEmailResponseSpec)
                        .extract().as(RegistrationResponseModel.class)
        );

        step("Проверяем получение ошибки в теле ответа после выполнения невалидного запроса", () ->
                assertThat("Не получили ошибку в теле ответа", response.getError(), containsString(ERROR_MESSAGE_PASSWORD))
        );
    }

    @Test
    @DisplayName("Проверяет ошибку регистрации при отсутствии ввода e-mail")
    void checkRegistrationWithoutEmailTest() {
        RegistrationBodyModel registration = new RegistrationBodyModel();
        registration.setPassword(PASSWORD);

        RegistrationResponseModel response = step("Выполняем запрос регистрации пользователя", () ->
                given(registrationRequestSpec)
                        .body(registration)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(missingPasswordOrEmailResponseSpec)
                        .extract().as(RegistrationResponseModel.class)
        );

        step("Проверяем получение ошибки в теле ответа после выполнения невалидного запроса", () ->
                assertThat("Не получили ошибку в теле ответа", response.getError(), containsString(ERROR_MESSAGE_EMAIL))
        );
    }
}