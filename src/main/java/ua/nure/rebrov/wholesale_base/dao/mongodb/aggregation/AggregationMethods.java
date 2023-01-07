package ua.nure.rebrov.wholesale_base.dao.mongodb.aggregation;

import ua.nure.rebrov.wholesale_base.model.Good;

public interface AggregationMethods {
    public void groupByCustomerAndCount();
    public void getGoodByMinOrders();
    public void getExecutedOrdersWithDelivery();
    public void getOrderAndPrice(int quantity);

    public void getOrderWithLongDelivery();
}
