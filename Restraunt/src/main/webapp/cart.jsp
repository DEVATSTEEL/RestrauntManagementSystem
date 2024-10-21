<%@page import="cn.restraunt.connection.DbCon"%>
<%@page import="cn.restraunt.dao.ProductDao"%>
<%@page import="cn.restraunt.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    DecimalFormat dcf = new DecimalFormat("#.##");
    User auth = (User) request.getSession().getAttribute("auth");
    ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");
    List<Cart> cartProduct = null;
    double total = 0;

    if (cart_list != null) {
        ProductDao pDao = new ProductDao(DbCon.getConnection());
        cartProduct = pDao.getCartProducts(cart_list);
        total = pDao.getTotalCartPrice(cart_list);
    }

    // Handle success/failure messages from servlet
    String message = request.getParameter("message");
    String alertMessage = "";
    String alertClass = "";

    if ("success".equals(message)) {
        alertMessage = "Order placed successfully!";
        alertClass = "alert-success";
    } else if ("failure".equals(message)) {
        alertMessage = "Failed to place the order.";
        alertClass = "alert-danger";
    } else if ("error".equals(message)) {
        alertMessage = "An error occurred while processing your request.";
        alertClass = "alert-danger";
    }
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="head.jsp"%>
<title>E-Commerce Cart</title>
<style type="text/css">
.table tbody td {
    vertical-align: middle;
}
.btn-incre, .btn-decre {
    box-shadow: none;
    font-size: 25px;
}
</style>
<script>
function updateQuantity(cartId, action) {
    const quantityInput = document.getElementById("quantity-" + cartId);
    let currentQuantity = parseInt(quantityInput.value);

    if (action === 'increment' && currentQuantity < 10) {
        currentQuantity++;
    } else if (action === 'decrement' && currentQuantity > 1) {
        currentQuantity--;
    }

    quantityInput.value = currentQuantity;
    calculateTotal(); // Recalculate the total price
}

function calculateTotal() {
    let total = 0;
    const cartRows = document.querySelectorAll('.cart-row');

    cartRows.forEach(row => {
        const price = parseFloat(row.getAttribute('data-price'));
        const quantity = parseInt(row.querySelector('.quantity-input').value);
        total += price * quantity;
    });

    document.getElementById('totalPrice').innerText = total.toFixed(2);
}

function updateHiddenQuantities() {
    const cartRows = document.querySelectorAll('.cart-row');

    cartRows.forEach(row => {
        const cartId = row.getAttribute('data-id'); // Get the cart item's ID
        const quantity = row.querySelector('.quantity-input').value; // Get the current quantity
        document.getElementById("hidden-quantity-" + cartId).value = quantity;
        console.log(`Cart ID: ${cartId}, Quantity: ${quantity}`); // Debugging: log values
    });
}
</script>
</head>
<body>

    <div class="container my-3">
        <% if (!alertMessage.isEmpty()) { %>
            <div class="alert <%= alertClass %>"><%= alertMessage %></div>
        <% } %>

        <div class="d-flex py-3">
            <h3>Total Price: ₹ <span id="totalPrice"><%= (total > 0) ? dcf.format(total) : 0 %></span></h3>
            <form action="cart-check-out" method="post" onsubmit="updateHiddenQuantities()">
                <button type="submit" class="mx-3 btn btn-primary">Check Out</button>

                <!-- Add hidden fields to store the updated quantities -->
                <%
                if (cart_list != null && cartProduct != null) {
                    for (Cart c : cartProduct) {
                %>
                <input type="hidden" name="quantities[<%= c.getId() %>]" id="hidden-quantity-<%= c.getId() %>" value="<%= c.getQuantity() %>">
                <%
                    }
                }
                %>
            </form>
        </div>

        <table class="table table-light">
            <thead>
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Category</th>
                    <th scope="col">Price</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Remove</th>
                </tr>
            </thead>
            <tbody>
                <%
                if (cart_list != null && cartProduct != null) {
                    for (Cart c : cartProduct) {
                %>
                <tr class="cart-row" data-id="<%= c.getId() %>" data-price="<%= c.getPrice() %>">
                    <td><%= c.getName() %></td>
                    <td><%= c.getCategory() %></td>
                    <td>₹ <%= dcf.format(c.getPrice()) %></td>
                    <td>
                        <a href="javascript:void(0);" onclick="updateQuantity(<%= c.getId() %>, 'decrement')" class="btn btn-sm btn-warning btn-decre">-</a>
                        <input type="text" id="quantity-<%= c.getId() %>" class="quantity-input" value="<%= c.getQuantity() %>" style="width: 40px; text-align: center;" readonly>
                        <a href="javascript:void(0);" onclick="updateQuantity(<%= c.getId() %>, 'increment')" class="btn btn-sm btn-success btn-incre">+</a>
                    </td>
                    <td><a href="remove-from-cart?id=<%= c.getId() %>" class="btn btn-sm btn-danger">Remove</a></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" class="text-center">Your cart is empty.</td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>

    <%@include file="footer.jsp"%>
</body>
</html>
