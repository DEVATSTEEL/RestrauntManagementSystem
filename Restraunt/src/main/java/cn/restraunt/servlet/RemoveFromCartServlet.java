package cn.restraunt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.restraunt.model.Cart;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String productId = request.getParameter("id");
            if (productId != null) {
                ArrayList<Cart> cartList = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");
                if (cartList != null) {
                    cartList.removeIf(c -> c.getId() == Integer.parseInt(productId)); // Efficiently remove the item
                }
                response.sendRedirect("cart.jsp");
            } else {
                response.sendRedirect("cart.jsp");
            }
        } catch (NumberFormatException e) {
            // Handle case where id is not a valid number
            response.sendRedirect("cart.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
