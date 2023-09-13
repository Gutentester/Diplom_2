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

public class EditUserTest {

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
    @DisplayName("Редактирование данных у авторизованного пользователя")
    public void editDataOfLoggedInUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        UserPOJO newUser = FakerUser.fakeUser();
        UserSteps.editLoggedInUser(token, newUser)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Редактирование данных у неавторизованного пользователя")
    public void editDataOfNonLoggedInUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        UserPOJO newUser = FakerUser.fakeUser();
        UserSteps.editNonLoggedInUser(token, newUser)
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
