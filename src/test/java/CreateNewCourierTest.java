import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import settings.CourierTestData;
import settings.RestClient;
import settings.Steps;

public class CreateNewCourierTest {

    public RestClient restClient = new RestClient();
    public Steps step = new Steps();
    CourierTestData courierTestData0 = new CourierTestData("courierTest1", "1234", "courierName1");
    CourierTestData courierTestData1 = new CourierTestData(null, "1234", "courierName31");
    CourierTestData courierTestData2 = new CourierTestData("courierTest32", null, "courierName32");


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
    @DisplayName("Тест №3: если одного из полей нет, запрос возвращает ошибку")
    public void allFieldsErrorCheck() {
        Response response1 = step.sendPostRequestV1Courier(courierTestData1);
        step.compareExpectedStatusCodeToFactual(response1, 400);
        step.compareExpectedErrorMessageToFactual(response1, "Недостаточно данных для создания учетной записи");

        Response response2 = step.sendPostRequestV1Courier(courierTestData2);
        step.compareExpectedStatusCodeToFactual(response2, 400);
        step.compareExpectedErrorMessageToFactual(response2, "Недостаточно данных для создания учетной записи");
    }
}
