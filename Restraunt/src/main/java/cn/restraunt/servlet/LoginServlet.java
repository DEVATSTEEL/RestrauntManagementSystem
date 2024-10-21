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
import cn.restraunt.dao.AdminDao;
import cn.restraunt.dao.UserDao;
import cn.restraunt.model.Admin;
import cn.restraunt.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String email = request.getParameter("login-email");
            String password = request.getParameter("login-password");

            // Check if email and password are provided
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                out.println("Missing login information. Please provide a valid email and password.");
                return;
            }

            AdminDao adminDao = new AdminDao(DbCon.getConnection());
            Admin admin = null;
            try {
                admin = adminDao.adminLogin(email, password);
            } catch (SQLException e) {
                out.println("Error occurred while logging in as admin: " + e.getMessage());
                e.printStackTrace(out); // Print stack trace to output for debugging
                return;
            }

            if (admin != null) {
                request.getSession().setAttribute("authAdmin", admin);
                response.sendRedirect("admin.jsp");
                return; // Exit after redirect
            }

            UserDao userDao = new UserDao(DbCon.getConnection());
            User user = null;
            user = userDao.userLogin(email, password);

            if (user != null) {
                request.getSession().setAttribute("auth", user);
                response.sendRedirect("index.jsp");
            } else {
                out.println("Invalid credentials. Please try again.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during login.");
        }
    }
}
