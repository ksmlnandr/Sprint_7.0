import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import settings.RestClient;
import settings.Steps;
import settings.order_response_body.OrderCreationBodyData;

@RunWith(Parameterized.class)
public class CreateNewOrderTest {
    public RestClient restClient = new RestClient();
    public Steps step = new Steps();

    private final String[] color;
    public CreateNewOrderTest(String[] color) {
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][][] colorInputData() {
        return new Object[][][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null},
        };
    }

    @Before
    public void setUp() { RestAssured.baseURI = restClient.getBaseUrl(); }

    @Test
    public void colorInputTest() {
        OrderCreationBodyData orderData = new OrderCreationBodyData(
                "Andrei",
                "Kosmylin",
                "Moscow Kremlin",
                "Teatralnaya",
                "+7 800 355 35 35",
                2,
                "2020-06-06",
                "Some additional comment",
                color
        );
        Response response = step.sendPostRequestV1Orders(orderData);
        step.compareExpectedStatusCodeToFactual(response, 201);
        step.compareExpectedShortResponseBodyMessageToFactual(response, "track");
    }
}
