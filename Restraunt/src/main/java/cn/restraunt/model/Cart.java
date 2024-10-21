package cn.restraunt.model;

public class Cart extends Product {
    private int quantity;  // Quantity of the product in the cart

    // Default constructor
    public Cart() {
    }

    // Constructor with parameters
    public Cart(int id, String name, String category, Double price, String image, int quantity) {
        super(id, name, category, price, image); // Call to Product constructor
        this.quantity = quantity; // Set the quantity
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Cart [Product=%s, quantity=%d]", super.toString(), quantity);
    }
}
