import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import settings.CourierTestData;
import settings.RestClient;
import settings.Steps;

import static io.restassured.RestAssured.given;

public class CourierAuthTest {
    private RestClient restClient = new RestClient();
    private Steps step = new Steps();
    private CourierTestData courierTestData0 = new CourierTestData("courierTest1", "1234", "courierName1");
    private CourierTestData getCourierTestData0() {
        return courierTestData0;
    }
    private CourierTestData courierTestData1 = new CourierTestData(null, "1234");
    private CourierTestData courierTestData2 = new CourierTestData("courierTest32", null);
    private CourierTestData courierTestData3 = new CourierTestData("fakeLogin_courierTest1", "1234");
    private CourierTestData courierTestData4 = new CourierTestData("courierTest1", "fakePasswordTest_1234");

    @Before
    public void setUp() { RestAssured.baseURI = restClient.getBaseUrl(); }

    @Test
    @DisplayName("Тест№1: существующий курьер может успешно авторизоваться")
    public void courierCorrectAuthCheck() {
        step.sendPostRequestV1Courier(courierTestData0);
        CourierTestData courierCredentials = new CourierTestData(courierTestData0.getLogin(), courierTestData0.getPassword());

        Response response = step.sendPostRequestV1CourierLogin(courierCredentials);
        step.compareExpectedStatusCodeToFactual(response, 200);
        step.compareExpectedShortResponseBodyMessageToFactual(response, "id");
    }

    @Test
    @DisplayName("Тест№2: для авторизации нужно передать обязательное поле login / если какого-то поля нет, запрос возвращает ошибку")
    public void loginRequiredFieldTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response1 = step.sendPostRequestV1CourierLogin(courierTestData1);
        step.compareExpectedStatusCodeToFactual(response1, 400);
        step.compareExpectedErrorMessageToFactual(response1, "Недостаточно данных для входа");
}

    @Test
    @DisplayName("Тест№3: для авторизации нужно передать все обязательное поле password / если какого-то поля нет, запрос возвращает ошибку")
    public void passwordRequiredFieldTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response2 = step.sendPostRequestV1CourierLogin(courierTestData2);
        step.compareExpectedStatusCodeToFactual(response2, 400);
        step.compareExpectedErrorMessageToFactual(response2, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Тест №4: система вернёт ошибку, если неправильно указать логин / если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void incorrectLoginTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response1 = step.sendPostRequestV1CourierLogin(courierTestData3);
        step.compareExpectedStatusCodeToFactual(response1, 404);
        step.compareExpectedErrorMessageToFactual(response1, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Тест №5: система вернёт ошибку, если неправильно указать пароль / если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void incorrectPasswordTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response2 = step.sendPostRequestV1CourierLogin(courierTestData4);
        step.compareExpectedStatusCodeToFactual(response2, 404);
        step.compareExpectedErrorMessageToFactual(response2, "Учетная запись не найдена");
    }

    @After
    public void cleanUp() {
        CourierTestData courierTestData = getCourierTestData0();
        CourierTestData courierCredentials = new CourierTestData(courierTestData.getLogin(), courierTestData.getPassword());
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCredentials)
                .when()
                .post(restClient.getCourierAuthCheck());
        int courierId = response.then().extract().body().path("id");
        String value = String.valueOf(courierId);

        if (value != null) {
            String json = String.format("{\"id\":\"%d\"}", courierId);

            Response response2 = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .delete(restClient.getCourierCreate());
            response2.then().assertThat().statusCode(200);
        }
    }
}
