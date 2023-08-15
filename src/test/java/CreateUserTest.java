import faker.FakerUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.UserPOJO;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {

    UserPOJO user;
    String token;

    @Before
    public void setUp() {
        user = FakerUser.fakeUser();
    }

    @After
    public void tearDown() {
        UserSteps.removeUser(token);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createNewUniqueUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUsedAccountUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей (password)")
    public void createUserWithoutPassword() {
        user = FakerUser.fakeUserWithoutPassword();
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей (name)")
    public void createUserWithoutName() {
        user = FakerUser.fakeUserWithoutName();
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей (email)")
    public void createUserWithoutEmail() {
        user = FakerUser.fakeUserWithoutEmail();
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
