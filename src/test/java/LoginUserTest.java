import com.github.javafaker.Faker;
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

public class LoginUserTest {

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
    @DisplayName("Логин под существующим пользователем")
    public void logInWithCorrectAccount() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        Response newResponse = UserSteps.logInUser(user);
        newResponse.then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным email")
    public void logInWithIncorrectEmail() {
        Faker faker = new Faker();
        UserSteps.createUser(user);
        UserPOJO incUser = new UserPOJO(faker.internet().emailAddress(), user.getPassword(), user.getName());
        UserSteps.logInUser(incUser)
                .then()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void logInWithIncorrectPassword() {
        Faker faker = new Faker();
        UserSteps.createUser(user);
        UserPOJO incUser = new UserPOJO(user.getEmail(), faker.internet().emailAddress(), user.getName());
        UserSteps.logInUser(incUser)
                .then()
                .assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
