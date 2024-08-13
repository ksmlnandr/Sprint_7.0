package settings.order_response_body;

import java.util.List;

public class AllOrdersData {
    private List<OrderCreationBodyData> orders;
    private PageInfoData pageInfo;
    private List<AvailableStationsData> availableStations;

    public List<OrderCreationBodyData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderCreationBodyData> orders) {
        this.orders = orders;
    }

    public PageInfoData getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoData pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStationsData> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStationsData> availableStations) {
        this.availableStations = availableStations;
    }
}
