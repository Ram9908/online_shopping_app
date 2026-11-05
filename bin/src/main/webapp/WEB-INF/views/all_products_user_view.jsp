<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<%@page import ="com.genpact.online_shopping_app.entity.User"%>
<%! int fontSize; %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>All Products</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
 <style>
        /* Your internal CSS styles go here */
        body {
            background-color: #eaf6ff;
            font-family: Arial, sans-serif;
        }
        .card {
            height: 550px; /* Adjust card height as needed */
            border: 1px solid #ccc;
            border-radius: 10px;
            width: 300px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden; /* Hide overflow content */
            display: flex;
            flex-direction: column;
        }
        .card img {
            max-width: 100%;
            height: auto;
            object-fit: cover; /* Ensure the image covers the entire container */
        }
        .card-body {
            flex: 1;
        }
        .card-footer {
            margin-top: auto;
        }
    </style>
</head>
<body>

<h2>All Products</h2>

<div class="d-flex flex-wrap flex-wrap-md gap-3 m-auto p-4">
      
<jsp:useBean id="productList" type="java.util.List" scope="request"/>  

<% User activeUser = (User) session.getAttribute("activeUser"); %>
<%
  for(Iterator<Product> it = productList.iterator(); it.hasNext(); ){
    Product row = (Product)it.next();
%>
    <div class="card m-auto col-2 p-2">
        <div class="text-center">
            <div class="card-header w-100">
                <img src="/Images/<%=row.getImageUrl()%>" alt="img">
            </div>
        </div>
        <div class="card-body text-center"> <!-- Center the content horizontally -->
            <div><h3><%= row.getProductName() %></h3></div>
            <div><%= row.getProductDescription() %></div>
            <div><%= row.getCategory() %></div>
            <div><h4>Rupees/- <%= row.getPrice() %></h4></div>
            <div><b>Stock Quantity : <%= row.getStockQuantity() %></b></div> 
        </div>
        <div class="card-footer">
            <form action="add/cart/<%=row.getProductId()%>" method="get" onsubmit="reloadPage()">
                <input type="hidden" name="userId" value="<%= activeUser.getUserId() %>"/>
                <div class="d-flex justify-content-center align-items-center">
                    <div class="w-25 mr-2">
                        <h6 class="mb-0">Quantity:</h6>
                    </div>
                    <input class="form-control mr-2" style="max-width: 60px;" type="number" max="<%= row.getStockQuantity() %>" value="1" name="quantity" placeholder="quantity">
                    <button type="submit" class="btn btn-success">Add to cart</button>
                </div>
            </form>
        </div>
    </div>
<%
  }  // for loop
%>

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
