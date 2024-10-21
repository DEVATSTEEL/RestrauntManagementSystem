<%@page import="cn.restraunt.connection.DbCon"%>
<%@page import="cn.restraunt.dao.ProductDao"%>
<%@page import="cn.restraunt.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.stream.Collectors"%>
<%@ include file = "includes/navbar.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    ProductDao pd = new ProductDao(DbCon.getConnection());
    List<Product> products = pd.getAllProducts();
    ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
    if (cart_list != null) {
        request.setAttribute("cart_list", cart_list);
    }

    // Get filter type from request
    String filter = request.getParameter("filter");
    if (filter != null && !filter.isEmpty()) {
        products = products.stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(filter))
            .collect(Collectors.toList());
    }
%>

<!DOCTYPE html>
<html>
<head>
<%@include file="head.jsp"%>
<title>E-Commerce Cart</title>
<script>
    function showSuccessMessage() {
        alert('Product successfully added to the cart!');
    }
</script>
</head>
<body>

<div class="container">
    <div class="card-header my-3">All Products</div>

    <!-- Filter Buttons -->
    <div class="mb-3">
        <form method="get" action="">
            <button type="submit" name="filter" value="" class="btn btn-secondary">All</button>
            <button type="submit" name="filter" value="Veg" class="btn btn-success">Veg</button>
            <button type="submit" name="filter" value="Non-Veg" class="btn btn-danger">Non-Veg</button>  
        </form>
    </div>

    <div class="row">
        <%
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
        %>
        <div class="col-md-3 my-3">
            <div class="card w-100">
                <img src="Food-Images/<%= p.getImage() %>" width="150" height="150" alt="<%= p.getName() %>">
                <div class="card-body">
                    <h5 class="card-title"><%= p.getName() %></h5>
                    <h6 class="price">Price: ₹<%= p.getPrice() %></h6>
                    <h6 class="category">Category: <%= p.getCategory() %></h6>
                    <div class="mt-3 d-flex justify-content-between">
                        <a class="btn btn-dark" href="add-to-cart?id=<%= p.getId() %>" onclick="showSuccessMessage()">Add to Cart</a>
                    </div>
                </div>
            </div>
        </div>
        <%
            }
        } else {
            out.println("<div class='col-12 text-center'>There are no products available.</div>");
        }
        %>
    </div>
</div>
<%@include file="footer.jsp"%>
    
</body>
</html>
