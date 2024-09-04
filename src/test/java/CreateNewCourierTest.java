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

public class CreateNewCourierTest {

    private RestClient restClient = new RestClient();
    private Steps step = new Steps();
    private CourierTestData courierTestData0 = new CourierTestData("courierTest1", "1234", "courierName1");
    private CourierTestData getCourierTestData0() {
        return courierTestData0;
    }
    private CourierTestData courierTestData1 = new CourierTestData(null, "1234", "courierName31");
    private CourierTestData courierTestData2 = new CourierTestData("courierTest32", null, "courierName32");


    @Before
    public void setUp() {
        RestAssured.baseURI = restClient.getBaseUrl();
    }

    @Test
    @DisplayName("Тест №1: курьера можно создать")
    public void newCourierCreationCheck() {
        Response response = step.sendPostRequestV1Courier(courierTestData0);
        step.compareExpectedStatusCodeToFactual(response, 201);
        step.compareExpectedShortResponseBodyMessageToFactual(response, "ok: true");
        step.deleteCourierDataWhenTestIsPassed(courierTestData0);
    }

    @Test
    @DisplayName("Тест №2: нельзя создать двух одинаковых курьеров")
    public void sameCourierCreationErrorCheck() {
        Response response = step.sendPostRequestV1Courier(courierTestData0);
        response = step.sendPostRequestV1Courier(courierTestData0);
        step.compareExpectedStatusCodeToFactual(response, 409);
        step.compareExpectedErrorMessageToFactual(response, "Этот логин уже используется");
        step.deleteCourierDataWhenTestIsPassed(courierTestData0);
    }

    @Test
    @DisplayName("Тест №3: если поле login не заполнено, запрос возвращает ошибку")
    public void emptyLoginErrorCheck() {
        Response response1 = step.sendPostRequestV1Courier(courierTestData1);
        step.compareExpectedStatusCodeToFactual(response1, 400);
        step.compareExpectedErrorMessageToFactual(response1, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Тест №4: если поле password не заполнено, запрос возвращает ошибку")
    public void emptyPasswordErrorCheck() {
        Response response2 = step.sendPostRequestV1Courier(courierTestData2);
        step.compareExpectedStatusCodeToFactual(response2, 400);
        step.compareExpectedErrorMessageToFactual(response2, "Недостаточно данных для создания учетной записи");
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
