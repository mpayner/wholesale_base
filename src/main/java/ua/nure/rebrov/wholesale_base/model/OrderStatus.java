package ua.nure.rebrov.wholesale_base.model;

public enum OrderStatus {
    Created("Створено"),
    Confirmed("Підтверджено"),
    Sent("Відправлено"),
    Delivered("Доставлено"),
    Cancelled("Скасовано");

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
