package settings;

public class RestClient {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_CREATE = "/api/v1/courier";
    private static final String COURIER_AUTH_CHECK = "/api/v1/courier/login";
    private static final String ORDER_ENDPOINT = "/api/v1/orders";

    public String getBaseUrl() {return BASE_URL;}
    public String getCourierCreate() {return COURIER_CREATE;}
    public String getCourierAuthCheck() {return COURIER_AUTH_CHECK;}
    public String getOrderEndpoint() {return ORDER_ENDPOINT;}

}
