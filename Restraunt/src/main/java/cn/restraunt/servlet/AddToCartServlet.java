package cn.restraunt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.restraunt.connection.DbCon;
import cn.restraunt.dao.ProductDao;
import cn.restraunt.model.Cart;
import cn.restraunt.model.Product;

@WebServlet(name = "AddToCartServlet", urlPatterns = "/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            ArrayList<Cart> cartList = (ArrayList<Cart>) session.getAttribute("cart-list");

            // If the cart list is null, create a new one
            if (cartList == null) {
                cartList = new ArrayList<>();
            }

            int productId = Integer.parseInt(request.getParameter("id"));

            // Fetch the product details from the database
            ProductDao productDao = new ProductDao(DbCon.getConnection());
            Product product = productDao.getProductById(productId); // Ensure this method exists in ProductDao

            if (product == null) {
                out.println("<h3 style='color:crimson; text-align: center'>Product not found. <a href='index.jsp'>Go back</a></h3>");
                return;
            }

            Cart newCartItem = new Cart();
            newCartItem.setId(product.getId()); // Use the fetched product ID
            newCartItem.setName(product.getName()); // Set the name based on the product
            newCartItem.setQuantity(1);

            boolean itemExists = false;

            // Check if the item is already in the cart
            for (Cart cartItem : cartList) {
                if (cartItem.getId() == productId) {
                    itemExists = true;
                    out.println("<h3 style='color:crimson; text-align: center'>Item Already in Cart. <a href='cart.jsp'>GO to Cart Page</a></h3>");
                    break; // Exit the loop if the item exists
                }
            }

            // If the item does not exist, add it to the cart
            if (!itemExists) {
                cartList.add(newCartItem);
                session.setAttribute("cart-list", cartList);
                response.sendRedirect("index.jsp");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to an error page if the ID is invalid
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to an error page if there's a DB error
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirect to an error page for class not found
        }
    }
}
