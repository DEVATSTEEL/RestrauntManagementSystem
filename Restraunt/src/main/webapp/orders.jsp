<%@ page import="java.sql.SQLException"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="cn.restraunt.dao.OrderDao"%>
<%@ page import="cn.restraunt.connection.DbCon"%>
<%@ page import="cn.restraunt.model.*"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Fetch user from session
    User auth = (User) request.getSession().getAttribute("auth");
    List<Order> orders = new ArrayList<>();

    // If user is logged in, retrieve their orders
    if (auth != null) {
        try {
            OrderDao orderDao = new OrderDao(DbCon.getConnection());
            orders = orderDao.userOrders(auth.getId());

            if (orders.isEmpty()) {
                request.setAttribute("message", "No orders found for this user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving orders. Please try again.");
        }
    } else {
        // Redirect to login page if not logged in
        response.sendRedirect("login.jsp");
        return;
    }

    // Set orders in request attribute to access in JSP
    request.setAttribute("orders", orders);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Orders</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container my-3">
    <div class="text-center">
        <a href="index.jsp" class="btn btn-secondary">Return to Home</a>
    </div>
</div>

<div class="container">
    <h2 class="my-3">Your Orders</h2>
    
    <%
        // Display a message if there are no orders or an error occurs
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <div class="alert alert-info text-center"><%= message %></div>
    <%
        }
    %>

    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Date</th>
                <th scope="col">Name</th>
                <th scope="col">Category</th>
                <th scope="col">Quantity</th>
                <th scope="col">Price</th>
                <th scope="col">Invoice</th>
            </tr>
        </thead>
        <tbody>
            <%
            if (orders != null && !orders.isEmpty()) {
                DecimalFormat dcf = new DecimalFormat("#.##"); // For formatting price
                for (Order o : orders) {
            %>
            <!-- Assign a unique ID to each row -->
            <tr id="orderRow<%= o.getOrderId() %>">
                <td><%= o.getDate() %></td>
                <td><%= o.getName() %></td>
                <td><%= o.getCategory() %></td>
                <td><%= o.getQuantity() %></td>
                <td><%= dcf.format(o.getPrice()) %></td>
                <td>
                    <!-- Add a custom print function for each row -->
                    <button class="btn btn-secondary btn-sm" onclick="printOrderRow(<%= o.getOrderId() %>)">Print</button>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="7" class="text-center">No orders found.</td>
            </tr>
            <%
            }
            %>
        </tbody>
    </table>
</div>

<!-- JavaScript for selective row printing -->
<script>
    function printOrderRow(orderId) {
        // Get the specific row to print
        var rowToPrint = document.getElementById("orderRow" + orderId);
        
        // Hide all rows except the one to print
        var allRows = document.querySelectorAll("tbody tr");
        allRows.forEach(row => {
            if (row !== rowToPrint) {
                row.style.display = "none";
            }
        });

        // Trigger print dialog
        window.print();

        // Restore all rows after printing
        allRows.forEach(row => {
            row.style.display = "";
        });
    }
</script>

<!-- Add Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
