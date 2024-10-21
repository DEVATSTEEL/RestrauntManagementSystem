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

@WebServlet("/quantity-inc-dec")
public class QuantityIncDecServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            int id = Integer.parseInt(request.getParameter("id"));
            ArrayList<Cart> cartList = (ArrayList<Cart>) request.getSession().getAttribute("cart-list");

            if (cartList != null && action != null && id >= 1) {
                boolean itemFound = false;
                for (Cart c : cartList) {
                    if (c.getId() == id) {
                        itemFound = true;
                        if (action.equals("inc")) {
                            c.setQuantity(c.getQuantity() + 1);
                        } else if (action.equals("dec")) {
                            if (c.getQuantity() > 1) {
                                c.setQuantity(c.getQuantity() - 1);
                            }
                        }
                        break;
                    }
                }
                if (itemFound) {
                    response.sendRedirect("cart.jsp");
                } else {
                    out.println("<h3 style='color:crimson; text-align: center'>Item not found in cart.</h3>");
                    response.sendRedirect("cart.jsp");
                }
            } else {
                response.sendRedirect("cart.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("cart.jsp");
        }
    }
}
