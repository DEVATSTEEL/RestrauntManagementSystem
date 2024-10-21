package cn.restraunt.model;

public class Order extends Product {
    private int orderId;
    private int uid; // User ID
    private int quantity; // Order quantity
    private String date; // Order date
    private double orderPrice; // Specific price for the order

    public Order() {
    }

    public Order(int orderId, int uid, int quantity, String date, double orderPrice) {
        super(); // Call the parent (Product) constructor if needed
        this.orderId = orderId;
        this.uid = uid;
        this.quantity = quantity;
        this.date = date;
        this.orderPrice = orderPrice; // Set the order price
    }

    public Order(int uid, int quantity, String date, double orderPrice) {
        super();
        this.uid = uid;
        this.quantity = quantity;
        this.date = date;
        this.orderPrice = orderPrice; // Set the order price
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }
}
