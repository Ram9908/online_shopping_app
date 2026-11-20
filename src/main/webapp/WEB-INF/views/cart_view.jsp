<%@ page import="java.util.List" %>
<%@ page import="com.genpact.online_shopping_app.entity.Cart" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<html>
<head>
    <title>Cart Items</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .qty-input {
            max-width: 80px;
        }
        .btn-delete-cart {
            padding: 0.2rem 0.55rem;
            font-size: 0.8rem;
            line-height: 1.2;
            border-radius: 6px;
        }
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-secondary p-2 justify-content-between align-items-center">
    <a href="<%= ctx %>/user" class="btn btn-light btn-sm m-2">Home</a>
    <a href="<%= ctx %>/view/all" class="btn btn-light btn-sm m-2">Products</a>
</div>
<jsp:include page="message_banner.jsp"/>

    <div class="container mt-5">
        <h2 class="text-center mb-4">Cart Items</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Product Image</th>
                    <th>Category</th>
                    <th>Product Description</th>
                    <th>Price</th>
                    <th>Update quantity</th>
                    <th>Delete cart</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Cart> cartItems = (List<Cart>) request.getAttribute("cartItems");
                    if (cartItems != null) {
                        for (Cart cartItem : cartItems) {
                        	List<Product> productList = (List<Product>) request.getAttribute("productList");
                        	for(Product product:productList){
                        	  if(product.getProductId().equals(cartItem.getvProductId())){ 
                %>
                <tr data-unit-price="<%= product.getPrice() %>">
                	<td><%= product.getProductName() %></td>
                	<td><img src="/Images/<%=product.getImageUrl()%>" style="max-height:270px;max-width:100%;width:auto;" alt="img"></td>
                    <td><%= product.getCategory() %></td>
                    <td><%= product.getProductDescription() %></td>
                    <td class="line-price"><%= product.getPrice()*cartItem.getQuantity() %></td>
                    <td>
                        <div class="d-flex align-items-center gap-2">
                            <h6 class="mb-0">Qty:</h6>
                            <input class="form-control qty-input" type="number" min="1"
                                   max="<%= product.getStockQuantity() %>"
                                   value="<%= cartItem.getQuantity() %>"
                                   data-cart-id="<%= cartItem.getCartId() %>"
                                   data-update-url="<%= ctx %>/view/all/update/<%= cartItem.getCartId() %>"
                                   onchange="autoUpdateQuantity(this)">
                        </div>
                    </td>
                    <td>
                     	<form action="<%= ctx %>/view/all/delete/<%= cartItem.getCartId() %>" method="get" class="m-0">
                            <button type="submit" class="btn btn-outline-danger btn-delete-cart">Delete</button>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    }
                        }
                    }
                %>
            </tbody>
        </table>

        <div class="text-center">
            <form action="<%= ctx %>/view/all/add/address" method="post">
                <button type="submit" class="btn btn-success">Place Order</button>
            </form>
            <div class="mt-2">Total Quantity: ${totalQuantity}</div>
        </div>
    </div>
   <script>
        function autoUpdateQuantity(input) {
            var url = input.getAttribute("data-update-url");
            var qty = parseInt(input.value, 10);
            var max = parseInt(input.getAttribute("max"), 10);
            var min = parseInt(input.getAttribute("min"), 10) || 1;
            if (isNaN(qty) || qty < min) {
                input.value = min;
                qty = min;
            }
            if (!isNaN(max) && qty > max) {
                input.value = max;
                qty = max;
            }
            input.disabled = true;
            fetch(url + "?quantity=" + encodeURIComponent(qty), {
                method: "GET",
                credentials: "same-origin",
                redirect: "follow"
            }).then(function (response) {
                input.disabled = false;
                if (response.ok || response.redirected) {
                    var row = input.closest("tr");
                    var unit = parseInt(row.getAttribute("data-unit-price"), 10) || 0;
                    var priceCell = row.querySelector(".line-price");
                    if (priceCell) {
                        priceCell.textContent = String(unit * qty);
                    }
                    window.location.reload();
                    return;
                }
                throw new Error("Could not update quantity.");
            }).catch(function () {
                input.disabled = false;
                window.location.reload();
            });
        }
    </script>
</body>
</html>
