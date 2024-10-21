package cn.restraunt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cn.restraunt.model.Order;

public class OrderDao {
    private Connection connection;

    public OrderDao(Connection connection) {
        this.connection = connection;
    }

    // Insert a new order into the 'orders' table
    public boolean insertOrder(Order order) {
        String query = "INSERT INTO orders (p_id, u_id, o_quantity, o_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = this.connection.prepareStatement(query)) {
            System.out.println("Attempting to insert order: ");
            System.out.println("Product ID: " + order.getId());
            System.out.println("User ID: " + order.getUid());
            System.out.println("Quantity: " + order.getQuantity());
            System.out.println("Date: " + order.getDate());

            // Set values in the prepared statement
            pst.setInt(1, order.getId());
            pst.setInt(2, order.getUid());
            pst.setInt(3, order.getQuantity());
            pst.setString(4, order.getDate());

            // Execute the update
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order inserted successfully!");
                return true;
            } else {
                System.out.println("Order insertion failed, no rows affected.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException while inserting order for product ID: " + order.getId());
            System.out.println("User ID: " + order.getUid() + ", Quantity: " + order.getQuantity() + ", Date: " + order.getDate());
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all orders for a specific user
    public List<Order> userOrders(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        // Join orders, products, and users tables to get the actual names
        String sql = "SELECT o.o_id, o.o_quantity, o.o_date, p.name AS product_name, p.category, p.price, u.name AS user_name " +
                     "FROM orders o " +
                     "JOIN products p ON o.p_id = p.id " + // Join with products to get product name and category
                     "JOIN users u ON o.u_id = u.id " +    // Join with users to get user name
                     "WHERE o.u_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set the user ID parameter

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("o_id"));
                    order.setUid(userId); // This remains the same since it's for the logged-in user
                    order.setQuantity(rs.getInt("o_quantity"));
                    order.setDate(rs.getString("o_date"));

                    // Set product details (inherited from Product)
                    order.setName(rs.getString("product_name")); // Set the product name
                    order.setCategory(rs.getString("category")); // Set the product category
                    order.setPrice(rs.getDouble("price")); // Set the product price

                    // Optionally, you can fetch and store the user name (although it might not be necessary for logged-in users)
                    String userName = rs.getString("user_name");
                    System.out.println("Order placed by user: " + userName);

                    orders.add(order);
                }
            }
        }

        return orders;
    }



    // Cancel a specific order
    public void cancelOrder(int orderId) {
        String query = "DELETE FROM orders WHERE o_id = ?"; // Ensure the column name is correct

        try (PreparedStatement pst = this.connection.prepareStatement(query)) {
            pst.setInt(1, orderId);
            int affectedRows = pst.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Order ID: " + orderId + " canceled successfully.");
            } else {
                System.out.println("Failed to cancel order. No order found with ID: " + orderId);
            }
        } catch (SQLException e) {
            // Capture detailed error information
            System.out.println("Error canceling order ID: " + orderId);
            e.printStackTrace();
        }
    }

public Order getOrderById(int orderId) throws SQLException {
    Order order = null;
    String query = "SELECT * FROM orders WHERE orderId = ?";

    try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            order = new Order();
            order.setOrderId(rs.getInt("orderId"));
            order.setName(rs.getString("name"));
            order.setCategory(rs.getString("category"));
            order.setQuantity(rs.getInt("quantity"));
            order.setPrice(rs.getDouble("price"));
            order.setDate(rs.getString("orderDate"));
            // Set other fields if necessary
        }
    }

    return order;
}
}