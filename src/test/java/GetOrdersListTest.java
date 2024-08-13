import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import settings.RestClient;
import settings.Steps;

public class GetOrdersListTest {

    public RestClient restClient = new RestClient();
    public Steps step = new Steps();

    @Before
    public void setUp() { RestAssured.baseURI = restClient.getBaseUrl(); }

    @Test
    @DisplayName("Тест №1: в тело ответа возвращается список заказов")
    public void OrdersListResponseTest() {
        Response response = step.sendGetRequestV1Orders();
        String responseJson = response.asString();
        Assert.assertNotEquals(null, responseJson);
    }

}
