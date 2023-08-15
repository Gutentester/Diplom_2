import faker.FakerUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.OrderPOJO;
import pojos.UserPOJO;
import steps.OrderSteps;
import steps.UserSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {

    UserPOJO user;
    OrderPOJO order;
    String token;
    String[] existingIngredients = new String[] {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa71"};
    String[] notExistingIngredients = new String[] {"3kj23kkk23211222", "ai7eww345sdaa81221"};


    @Before
    public void setUp() {
        user = FakerUser.fakeUser();
        Response response = UserSteps.createUser(user);
        token = response.then().extract().path("accessToken").toString();
    }

    @After
    public void tearDown() {
        UserSteps.removeUser(token);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("C ингредиентами и выполненной авторизацией")
    public void createOrderWithIngredientsAndCompletedAuth() {
        order = new OrderPOJO(existingIngredients);
        OrderSteps.createOrderWithAuth(token, order)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("C ингредиентами без авторизации")
    public void createOrderWithIngredientsAndNotCompletedAuth() {
        order = new OrderPOJO(existingIngredients);
        OrderSteps.createOrderWithoutAuth(order)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Без ингредиентов и выполненной авторизацией")
    public void createOrderWithoutIngredientsAndCompleteAuth() {
        order = new OrderPOJO();
        OrderSteps.createOrderWithAuth(token, order)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("С некорректными хешами ингредиентов и выполненной авторезацией")
    public void createOrderWithInvalidIngredientsAndCompleteAuth() {
        order = new OrderPOJO(notExistingIngredients);
        OrderSteps.createOrderWithAuth(token, order)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}