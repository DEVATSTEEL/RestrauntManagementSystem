package cn.restraunt.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.restraunt.connection.DbCon;
import cn.restraunt.dao.OrderDao;
import cn.restraunt.model.Cart;
import cn.restraunt.model.Order;
import cn.restraunt.model.User;

@WebServlet("/cart-check-out")
public class CheckOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Entering CheckOutServlet");

        @SuppressWarnings("unchecked")
        ArrayList<Cart> cartList = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");
        User auth = (User) request.getSession().getAttribute("auth");

        // Check if the cart is empty or user is not authenticated
        if (cartList == null || cartList.isEmpty() || auth == null) {
            System.out.println("Cart is empty or user is not authenticated. Redirecting...");
            response.sendRedirect("cart.jsp?message=error");
            return;
        }

        Connection connection = null;
        boolean allOrdersPlaced = true;
        try {
            connection = DbCon.getConnection();
            System.out.println("Database connection established.");

            OrderDao orderDao = new OrderDao(connection);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());

            // Iterate through the cart items and place orders
            for (Cart cartItem : cartList) {
                Order order = new Order();
                order.setId(cartItem.getId());
                order.setUid(auth.getId());

                // Get the updated quantity from the request
                String[] quantities = request.getParameterValues("quantities[" + cartItem.getId() + "]");
                if (quantities != null && quantities.length > 0) {
                    order.setQuantity(Integer.parseInt(quantities[0]));
                } else {
                    order.setQuantity(cartItem.getQuantity()); // fallback if no quantity is found
                }

                order.setDate(formatter.format(sqlDate));

                // Log the details of the order being placed
                System.out.println("Placing order: Product ID = " + order.getId() +
                                   ", User ID = " + order.getUid() +
                                   ", Quantity = " + order.getQuantity() +
                                   ", Date = " + order.getDate());

                if (!orderDao.insertOrder(order)) {
                    allOrdersPlaced = false;
                    System.out.println("Failed to place order for Product ID: " + order.getId());
                    break;
                }
            }

            // Clear cart and redirect based on the result
            if (allOrdersPlaced) {
                System.out.println("All orders placed successfully. Clearing the cart.");
                cartList.clear();
                request.getSession().setAttribute("cart-list", cartList);
                response.sendRedirect("orders.jsp?message=success");
            } else {
                response.sendRedirect("cart.jsp?message=failure");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cart.jsp?message=error");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
