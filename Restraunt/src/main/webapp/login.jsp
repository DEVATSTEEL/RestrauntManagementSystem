<%@page import="cn.restraunt.dao.*"%>
<%@page import="cn.restraunt.model.*"%>
<%@page import="java.sql.*"%>
<%@page import="cn.restraunt.connection.DbCon"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
<head>
    <%@include file="head.jsp"%>
    <title>E-Commerce Login</title>
</head>
<body>

<%
    String message = "";
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String email = request.getParameter("login-email");
        String password = request.getParameter("login-password");

        // Basic validation for empty fields
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            message = "Please provide both email and password.";
        } else {
            try {
                // Establish database connection
                UserDao userDao = new UserDao(DbCon.getConnection());
                AdminDao adminDao = new AdminDao(DbCon.getConnection());

                // Attempt admin login
                Admin admin = adminDao.adminLogin(email, password);
                if (admin != null) {
                    request.getSession().setAttribute("admin", admin);
                    response.sendRedirect("admin.jsp");
                    return;
                }

                // Attempt user login
                User user = userDao.userLogin(email, password);
                if (user != null) {
                    request.getSession().setAttribute("auth", user);
                    request.getSession().setAttribute("u_id", user.getId());
                    response.sendRedirect("index.jsp");
                    return;
                } else {
                    message = "Invalid email or password. Please try again.";
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = "An error occurred during login. Please try again later.";
            }
        }
    }
%>

<div class="container">
    <div class="card w-50 mx-auto my-5">
        <div class="card-header text-center">Login</div>
        <div class="card-body">
            <!-- Update form action to point to the correct servlet URL -->
            <form action="login" method="post">
                <div class="form-group">
                    <label>Email address</label>
                    <input type="email" name="login-email" class="form-control" placeholder="Enter email" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="login-password" class="form-control" placeholder="Enter password" required>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>
                <!-- Display any login messages (errors) -->
                <% if (!message.isEmpty()) { %>
                    <div class="alert alert-danger mt-3"><%= message %></div>
                <% } %>
            </form>
            <p class="text-center mt-3">Don't have an account? <a href="SignUp.jsp">Sign up</a></p>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>
</body>
</html>
