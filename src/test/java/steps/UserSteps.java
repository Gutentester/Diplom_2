package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojos.UserPOJO;

import static constants.Constants.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step
    public static Response createUser(UserPOJO user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(BASE_URI + CREATE_USER);
        return response;
    }

    @Step
    public static Response logInUser(UserPOJO user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(BASE_URI + LOGIN);
        return response;
    }

    @Step
    public static void removeUser(String token) {
        if (token != null) {
            given()
                    .header("Authorization", token)
                    .delete(BASE_URI + LOGIN);
        }
    }

    @Step
    public static Response editLoggedInUser(String token, UserPOJO user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .patch(BASE_URI + USER_INFORMATION);
        return response;
    }

    @Step
    public static Response editNonLoggedInUser(String token, UserPOJO user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(BASE_URI + USER_INFORMATION);
        return response;
    }

}
