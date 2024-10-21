<%@ page import="java.sql.SQLException" %>
<%@ page import="cn.restraunt.dao.OrderDao" %>
<%@ page import="cn.restraunt.connection.DbCon" %>
<%@ page import="cn.restraunt.model.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    int orderId = Integer.parseInt(request.getParameter("id"));
    Order order = null;

    try {
        OrderDao orderDao = new OrderDao(DbCon.getConnection());
        order = orderDao.getOrderById(orderId);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    if (order == null) {
        request.setAttribute("errorMessage", "Order not found.");
        response.sendRedirect("orders.jsp"); // Redirect if order not found
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice for Order #<%= order.getOrderId() %></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container my-3">
    <h2>Invoice for Order #<%= order.getOrderId() %></h2>
    <table class="table table-bordered">
        <tr>
            <th>Name:</th>
            <td><%= order.getName() %></td>
        </tr>
        <tr>
            <th>Category:</th>
            <td><%= order.getCategory() %></td>
        </tr>
        <tr>
            <th>Quantity:</th>
            <td><%= order.getQuantity() %></td>
        </tr>
        <tr>
            <th>Price:</th>
            <td><%= order.getPrice() %></td>
        </tr>
        <tr>
            <th>Date:</th>
            <td><%= order.getDate() %></td>
        </tr>
    </table>

    <div class="text-center">
        <a href="orders.jsp" class="btn btn-secondary">Back to Orders</a>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
