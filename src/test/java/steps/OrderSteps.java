package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojos.OrderPOJO;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step
    public static Response getOrdersLoggedInUser(String token) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .when()
                .get(BASE_URI + ORDER_INFORMATION);
        return response;
    }

    @Step
    public static Response getOrdersNonLoggedInUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URI + ORDER_INFORMATION);
        return response;
    }

    @Step
    public static Response createOrderWithAuth(String token, OrderPOJO order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(BASE_URI + ORDER_INFORMATION);
        return response;
    }

    @Step
    public static Response createOrderWithoutAuth(OrderPOJO order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(BASE_URI + ORDER_INFORMATION);
        return response;
    }
}
