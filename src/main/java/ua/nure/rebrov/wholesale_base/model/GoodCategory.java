package ua.nure.rebrov.wholesale_base.model;

public class GoodCategory {
    Integer idCategory;
    String name;
    Integer subcategory;

    public GoodCategory(){}

    public GoodCategory(Integer idCategory, String name, Integer subcategory) {
        this.idCategory = idCategory;
        this.name = name;
        this.subcategory = subcategory;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Integer subcategory) {
        this.subcategory = subcategory;
    }
}
