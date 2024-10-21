package cn.restraunt.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private Double price;
    private String image;

    // Default constructor
    public Product() {
    }

    // Constructor with parameters
    public Product(int id, String name, String category, Double price, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return String.format("Product [id=%d, name=%s, category=%s, price=%.2f, image=%s]", 
                id, name, category, price, image);
    }
}
