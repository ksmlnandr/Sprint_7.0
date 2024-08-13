import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import settings.CourierTestData;
import settings.RestClient;
import settings.Steps;

public class CourierAuthTest {
    public RestClient restClient = new RestClient();
    public Steps step = new Steps();
    CourierTestData courierTestData0 = new CourierTestData("courierTest1", "1234", "courierName1");
    CourierTestData courierTestData1 = new CourierTestData(null, "1234");
    CourierTestData courierTestData2 = new CourierTestData("courierTest32", null);
    CourierTestData courierTestData3 = new CourierTestData("fakeLogin_courierTest1", "1234");
    CourierTestData courierTestData4 = new CourierTestData("courierTest1", "fakePasswordTest_1234");

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

        step.deleteCourierDataWhenTestIsPassed(courierTestData0);
    }

    @Test
    @DisplayName("Тест№2: для авторизации нужно передать все обязательные поля / если какого-то поля нет, запрос возвращает ошибку")
    public void courierRequiredFieldsTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response1 = step.sendPostRequestV1CourierLogin(courierTestData1);
        step.compareExpectedStatusCodeToFactual(response1, 400);
        step.compareExpectedErrorMessageToFactual(response1, "Недостаточно данных для входа");

        Response response2 = step.sendPostRequestV1CourierLogin(courierTestData2);
        step.compareExpectedStatusCodeToFactual(response2, 400);
        step.compareExpectedErrorMessageToFactual(response2, "Недостаточно данных для входа");

        step.deleteCourierDataWhenTestIsPassed(courierTestData0);
    }

    @Test
    @DisplayName("Тест №3: система вернёт ошибку, если неправильно указать логин или пароль / если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void incorrectCredentialsTest() {
        step.sendPostRequestV1Courier(courierTestData0);

        Response response1 = step.sendPostRequestV1CourierLogin(courierTestData3);
        step.compareExpectedStatusCodeToFactual(response1, 404);
        step.compareExpectedErrorMessageToFactual(response1, "Учетная запись не найдена");

        Response response2 = step.sendPostRequestV1CourierLogin(courierTestData4);
        step.compareExpectedStatusCodeToFactual(response2, 404);
        step.compareExpectedErrorMessageToFactual(response2, "Учетная запись не найдена");

        step.deleteCourierDataWhenTestIsPassed(courierTestData0);
    }
}
