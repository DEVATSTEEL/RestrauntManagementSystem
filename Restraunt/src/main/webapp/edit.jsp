<%@page import="cn.restraunt.connection.DbCon"%>
<%@page import="cn.restraunt.model.Product"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>

<%
    String productId = request.getParameter("id");
    Product product = null;

    // Debugging output
    out.println("<p>Product ID: " + productId + "</p>");

    try {
        Connection conn = DbCon.getConnection();
        String query = "SELECT * FROM products WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, productId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getDouble("price"));
            product.setImage(rs.getString("image"));
        } else {
            out.println("<p>No product found with ID: " + productId + "</p>");
        }
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h2>Edit Product</h2>
        <% if (product != null) { %>
            <form action="update-product" method="post">
                <input type="hidden" name="id" value="<%= product.getId() %>">
                <div class="form-group">
                    <label for="name">Product Name</label>
                    <input type="text" name="name" class="form-control" value="<%= product.getName() %>" required>
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <input type="text" name="category" class="form-control" value="<%= product.getCategory() %>" required>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" step="0.01" name="price" class="form-control" value="<%= product.getPrice() %>" required>
                </div>
                <div class="form-group">
                    <label for="image">Image URL</label>
                    <input type="text" name="image" class="form-control" value="<%= product.getImage() %>" required>
                </div>
                <button type="submit" class="btn btn-primary">Update Product</button>
            </form>
        <% } else { %>
            <p>Product not found!</p>
        <% } %>
    </div>
</body>
</html>
