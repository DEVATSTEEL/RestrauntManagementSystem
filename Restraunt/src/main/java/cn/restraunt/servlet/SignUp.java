package cn.restraunt.servlet;

import cn.restraunt.model.User;
import cn.restraunt.dao.UserDao;
import cn.restraunt.connection.DbCon;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SignUp() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve parameters from the request
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate required fields
        if (isAnyFieldEmpty(name, email, password, confirmPassword)) {
            request.setAttribute("error", "All fields are required!");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("SignUp.jsp").forward(request, response);
            return;
        }

        try {
            // Get a database connection
            Connection con = DbCon.getConnection();
            UserDao userDao = new UserDao(con);

            // Create a new user
            User user = new User(name, email, password);
            boolean isRegistered = userDao.registerUser(user);

            // Check if registration was successful
            if (isRegistered) {
                response.sendRedirect("index.jsp");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    // Helper method to check for empty fields
    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
