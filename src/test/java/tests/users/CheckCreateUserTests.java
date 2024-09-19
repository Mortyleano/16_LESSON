package tests.users;

import models.CreateUserBodyModel;
import models.CreateUserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.CreateUserSpec.*;

@Tag("smoke")
@DisplayName("Проверяет API метод для создания пользователя")
public class CheckCreateUserTests extends TestBase {

    public static final String USER_NAME = "Alexander";
    public static final String USER_JOB = "QA";
    private static final String API_PATH = "/users";

    @Test
    @DisplayName("Проверяет успешное создание пользователя при вводе всех полей")
    void checkCreateUserTestWithNameAndJob() {
        CreateUserBodyModel createUser = new CreateUserBodyModel();
        createUser.setName(USER_NAME);
        createUser.setJob(USER_JOB);

        CreateUserResponseModel response = step("Выполняем запрос создания пользователя", () ->
                given(createUserRequestSpec)
                        .body(createUser)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class)
        );

        step("Проверяем получение имени пользователя в теле ответа", () ->
                assertThat("Получили невалидное имя", response.getName(), containsString(USER_NAME))
        );

        step("Проверяем получение наименование профессии пользователя в теле ответа", () ->
                assertThat("Получили невалидное наименование профессии", response.getJob(), containsString(USER_JOB))
        );
    }

    @Test
    @DisplayName("Проверяет создание пользователя при вводе имени без наименования профессии")
    void checkCreateUserTestWithoutJob() {
        CreateUserBodyModel createUser = new CreateUserBodyModel();
        createUser.setName(USER_NAME);

        CreateUserResponseModel response = step("Выполняем запрос создания пользователя", () ->
                given(createUserRequestSpec)
                        .body(createUser)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class)
        );

        step("Проверяем поле имени пользователя в теле ответа", () ->
                assertThat("Получили невалидное имя", response.getName(), containsString(USER_NAME)
        ));

        step("Проверяем поле с наименованием профессии пользователя в теле ответа", () ->
                assertThat("В поле наименования профессии пришло значение", response.getJob(), nullValue()
        ));
    }

    @Test
    @DisplayName("Проверяет создание пользователя при вводе наименования профессии без имени")
    void checkCreateUserTestWithoutName() {
        CreateUserBodyModel createUser = new CreateUserBodyModel();
        createUser.setJob(USER_JOB);

        CreateUserResponseModel response = step("Выполняем запрос создания пользователя", () ->
                given(createUserRequestSpec)
                        .body(createUser)
                        .when()
                        .post(API_PATH)
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class)
        );

        step("Проверяем поле имени пользователя в теле ответа", () ->
                assertThat("В поле имени пользователя пришло значение", response.getName(), nullValue())
        );

        step("Проверяем поле с наименованием профессии пользователя в теле ответа", () ->
                assertThat("Получили невалидное наименование профессии", response.getJob(), containsString(USER_JOB))
        );
    }
}