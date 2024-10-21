<%@ page import="java.sql.*, java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Product Management</title>
<!-- Add Bootstrap CSS -->
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<style>
/* Add some custom styles */
.container {
	margin-top: 50px;
}

.btn-toggle {
	margin-bottom: 10px;
}
</style>
</head>
<body>

	<div class="container">
		<h1 class="text-center">Product Management</h1>

		<div class="row">
			<!-- Add Product Form -->
			<div class="col-md-6">
				<h2>Add Product</h2>
				<!-- Toggle Button for Add Product Form -->
				<button class="btn btn-primary btn-toggle" type="button"
					data-toggle="collapse" data-target="#addProductForm"
					aria-expanded="false">Add Product</button>
				<div class="collapse" id="addProductForm">
					<form action="admin.jsp" method="POST" class="form-group mt-2">
						<input type="text" name="name" class="form-control"
							placeholder="Product Name" required><br> <input
							type="text" name="category" class="form-control"
							placeholder="Category" required><br> <input
							type="number" name="price" class="form-control"
							placeholder="Price" step="0.01" required><br> <input
							type="text" name="image" class="form-control"
							placeholder="Image URL" required><br> <input
							type="submit" name="action" class="btn btn-success"
							value="Add Product">
					</form>
				</div>
			</div>

			<!-- Edit Product Form -->
			<div class="col-md-6">
				<%
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String url = "jdbc:mysql://localhost:3306/jp";
				String dbUser = "root";
				String pass = "root";

				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(url, dbUser, pass);
					stmt = conn.createStatement();

					if (request.getParameter("editId") != null) {
						int editId = Integer.parseInt(request.getParameter("editId"));
						rs = stmt.executeQuery("SELECT * FROM products WHERE id = " + editId);
						if (rs.next()) {
					String editName = rs.getString("name");
					String editCategory = rs.getString("category");
					double editPrice = rs.getDouble("price");
					String editImage = rs.getString("image");
				%>
				<form action="admin.jsp" method="POST" class="form-group">
					<input type="hidden" name="id" value="<%=editId%>"> <input
						type="text" name="name" class="form-control"
						value="<%=editName%>" required><br> <input
						type="text" name="category" class="form-control"
						value="<%=editCategory%>" required><br> <input
						type="number" name="price" class="form-control"
						value="<%=editPrice%>" step="0.01" required><br> <input
						type="text" name="image" class="form-control"
						value="<%=editImage%>" required><br> <input
						type="submit" name="action" class="btn btn-success"
						value="Update Product">
				</form>
				<%
				}
				} else {
				%>
				<%
				}
				} catch (Exception e) {
				e.printStackTrace();
				} finally {
				try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				} catch (SQLException e) {
				e.printStackTrace();
				}
				}
				%>
			</div>
		</div>

		<hr>

		<h2>Product List</h2>
		<table class="table table-bordered">
			<thead class="thead-dark">
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Category</th>
					<th>Price</th>
					<th>Image</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<%
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(url, dbUser, pass);
					stmt = conn.createStatement();

					if ("Add Product".equals(request.getParameter("action"))) {
						String name = request.getParameter("name");
						String category = request.getParameter("category");
						double price = Double.parseDouble(request.getParameter("price"));
						String image = request.getParameter("image");

						String insertQuery = "INSERT INTO products (name, category, price, image) VALUES ('" + name + "', '" + category
						+ "', " + price + ", '" + image + "')";
						stmt.executeUpdate(insertQuery);
					}

					if ("Delete".equals(request.getParameter("action"))) {
						int id = Integer.parseInt(request.getParameter("id"));
						String deleteQuery = "DELETE FROM products WHERE id = " + id;
						stmt.executeUpdate(deleteQuery);
					}

					if ("Update Product".equals(request.getParameter("action"))) {
						int id = Integer.parseInt(request.getParameter("id"));
						String name = request.getParameter("name");
						String category = request.getParameter("category");
						double price = Double.parseDouble(request.getParameter("price"));
						String image = request.getParameter("image");

						String updateQuery = "UPDATE products SET name='" + name + "', category='" + category + "', price=" + price
						+ ", image='" + image + "' WHERE id = " + id;
						stmt.executeUpdate(updateQuery);
					}

					rs = stmt.executeQuery("SELECT * FROM products");
					while (rs.next()) {
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String category = rs.getString("category");
						double price = rs.getDouble("price");
						String image = rs.getString("image");
				%>
				<tr>
					<td><%=id%></td>
					<td><%=name%></td>
					<td><%=category%></td>
					<td><%=price%></td>
					<td><img src="Food-Images/<%=image%>" width="150"
						height="150" alt="<%=name%>"></td>

					<td>
						<!-- Collapse Edit Form -->
						<button class="btn btn-info btn-sm btn-toggle" type="button"
							data-toggle="collapse" data-target="#editForm<%=id%>"
							aria-expanded="false">Edit</button>
						<div class="collapse" id="editForm<%=id%>">
							<form action="admin.jsp" method="POST" class="form-group">
								<input type="hidden" name="id" value="<%=id%>"> <input
									type="text" name="name" class="form-control"
									value="<%=name%>" required><br> <input
									type="text" name="category" class="form-control"
									value="<%=category%>" required><br> <input
									type="number" name="price" class="form-control"
									value="<%=price%>" step="0.01" required><br> <input
									type="text" name="image" class="form-control"
									value="<%=image%>" required><br> <input
									type="submit" name="action" class="btn btn-success"
									value="Update Product">
							</form>
						</div> <!-- Delete Product -->
						<form action="admin.jsp" method="POST" style="display: inline;">
							<input type="hidden" name="id" value="<%=id%>"> <input
								type="submit" name="action" class="btn btn-danger btn-sm"
								value="Delete">
						</form>
					</td>
				</tr>
				<%
				}
				} catch (Exception e) {
				e.printStackTrace();
				} finally {
				try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				} catch (SQLException e) {
				e.printStackTrace();
				}
				}
				%>
			</tbody>
		</table>

		<!-- Add Bootstrap JS and dependencies -->
		<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
		<script
			src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
		<script
			src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	</div>

</body>
</html>
