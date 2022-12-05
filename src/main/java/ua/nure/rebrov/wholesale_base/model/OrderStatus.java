package ua.nure.rebrov.wholesale_base.model;

import java.util.*;

public enum OrderStatus {
    Created("Створено"),
    Confirmed("Підтверджено"),
    Sent("Відправлено"),
    Delivered("Доставлено"),
    Done("Виконано"),
    Cancelled("Скасовано");

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static OrderStatus contains(String title){
        OrderStatus[] values = values();
        for(OrderStatus o : values){
            if(o.getTitle().equals(title)){
                return o;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public static List<OrderStatus> availableStatuses(Order order){
        List<OrderStatus> statusList = null;
        switch (order.getStatus()){
            case Created:
                statusList = Arrays.asList(OrderStatus.Confirmed, OrderStatus.Cancelled);
                break;
            case Confirmed:
                statusList = Arrays.asList(OrderStatus.Sent, OrderStatus.Cancelled);
                break;
            case Sent:
                statusList = List.of(OrderStatus.Delivered);
                break;
            case Delivered:
                statusList = Arrays.asList(OrderStatus.Done, OrderStatus.Cancelled);
                break;
        }
        return statusList;
    }

    public static Map<Order, List<OrderStatus>> availableStatusesForOrders(List<Order> list){
        Map<Order, List<OrderStatus>> listMap = new LinkedHashMap<>();

        for(Order o: list){
            listMap.put(o, availableStatuses(o));
        }
        return listMap;
    }
}
