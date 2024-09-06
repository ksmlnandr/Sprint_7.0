package settings;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import settings.order.OrderCreationBodyData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Steps {

    RestClient restClient = new RestClient();

    @Step("Вызван метод создания курьера с заполненным телом запроса")
    public Response sendPostRequestV1Courier(CourierTestData courierTestData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierTestData)
                        .when()
                        .post(restClient.getCourierCreate());
        return response;
    }

    @Step("Вызван метод авторизации курьера с заполненным телом запроса")
    public Response sendPostRequestV1CourierLogin(CourierTestData courierTestData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierTestData)
                        .when()
                        .post(restClient.getCourierAuthCheck());
        return response;
    }

    @Step("Удаление данных о курьере после успешного теста")
    public void deleteCourierDataWhenTestIsPassed(CourierTestData courierTestData) {
        CourierTestData courierCredentials = new CourierTestData(courierTestData.getLogin(), courierTestData.getPassword());
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCredentials)
                .when()
                .post(restClient.getCourierAuthCheck());
        int courierId = response.then().extract().body().path("id");
        String json = String.format("{\"id\":\"%d\"}",courierId);

        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .delete(restClient.getCourierCreate());
        response2.then().assertThat().statusCode(200);
    }

    @Step("Вызван метод создания заказа с указанием значения параметра color в теле запроса")
    public Response sendPostRequestV1Orders(OrderCreationBodyData orderData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderData)
                        .when()
                        .post(restClient.getOrderEndpoint());
        return response;
    }

    @Step("Вызван метод получения списка всех заказов")
    public Response sendGetRequestV1Orders() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get(restClient.getOrderEndpoint());
        return response;
    }

    @Step("Получен ожидаемый статус-код")
    public void compareExpectedStatusCodeToFactual(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("Получено ожидаемое сообщение об ошибке в теле ответа")
    public void compareExpectedErrorMessageToFactual(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }

    @Step("Тело ответа содержит ожидаемое значение")
    public void compareExpectedShortResponseBodyMessageToFactual(Response response, String message) {
        response.asString().contains(message);
    }
}