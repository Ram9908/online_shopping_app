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
    </style>
    
</head>
<body>

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
                    <th>Quantity</th>
                    <th>Update cart</th>
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
                <tr>
                	<td><%= product.getProductName() %></td>
                	<td> <img src="/Images/<%=product.getImageUrl()%>" style="max-height:270px;max-width:100%;width:auto;"  alt="img"></td>
                    <td><%= product.getCategory() %></td>
                    <td><%= product.getProductDescription() %></td>
                    <td><%= product.getPrice()*cartItem.getQuantity() %></td>
                    <td><%= cartItem.getQuantity() %></td>
                    <td>
                    <form action="update/<%= cartItem.getCartId() %>" method="get" onsubmit="reloadPage()">
	                    <input type="number" name="quantity" value="<%= cartItem.getQuantity() %>" >
	                    <button type="submit" class="btn btn-primary">Update Quantity</button>
                    </form>
                    </td>
                    <td>
                     	<form action="delete/<%= cartItem.getCartId() %>" method="get" onsubmit="reloadPage()">
                            <input type="hidden" name="cartItemId" value="<%= cartItem.getCartId() %>">
                            <button type="submit" class="btn btn-danger">Delete Product</button>
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
            <form action="add/address" method="post" onsubmit="reloadPage()">
                <button type="submit" class="btn btn-success">Place Order for Entire Cart</button>               
            </form>
            Total Quantity: ${totalQuantity}
        </div>     
    </div>
   <script>
        
        function reloadPage() {
            setTimeout(function() {
                location.reload();
            }, 1000); // Reload after 1 second
        }
    </script>
     
</body>
</html>
