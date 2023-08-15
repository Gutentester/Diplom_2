import faker.FakerUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.UserPOJO;
import steps.OrderSteps;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {

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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersLoggedInUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        OrderSteps.getOrdersLoggedInUser(token)
                .then().statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersNonLoggedInUser() {
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
        OrderSteps.getOrdersNonLoggedInUser()
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
