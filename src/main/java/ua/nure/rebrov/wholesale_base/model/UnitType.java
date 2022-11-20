package ua.nure.rebrov.wholesale_base.model;

public enum UnitType {
    Box("Ящик"), Pack("Вроздріб");

    private String title;

    UnitType(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
