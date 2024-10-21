package cn.restraunt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.restraunt.connection.DbCon;
import cn.restraunt.dao.OrderDao;

@WebServlet("/cancel-order")
public class CancelOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String id = request.getParameter("id");
            if (id != null) {
                try {
                    OrderDao orderDao = new OrderDao(DbCon.getConnection());
                    orderDao.cancelOrder(Integer.parseInt(id));
                    response.sendRedirect("orders.jsp?message=Order canceled successfully");
                } catch (SQLException e) {
                    out.println("<h3 style='color:red;'>Error canceling order: " + e.getMessage() + "</h3>");
                    response.sendRedirect("orders.jsp?error=Error canceling order");
                } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
                response.sendRedirect("orders.jsp?error=Invalid order ID");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("orders.jsp?error=Invalid order ID format");
        }
    }
}
