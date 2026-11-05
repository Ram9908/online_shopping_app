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
            border: 1px solid #ccc;
            border-radius: 10px;
            width: 300px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .image-container {
            height: 200px; /* Adjust this value as needed */
            overflow: hidden;
            text-align: center;
        }
        .image-container img {
            max-width: 100%;
            max-height: 100%;
        }
       
    </style>
</head>
<body>

<h2>All Products</h2>

<div class="d-flex flex-wrap flex-wrap-md gap-3 m-auto p-4">
      
<jsp:useBean id="productItems" type="java.util.List" scope="request"/>  

<%
  for(Iterator<Product> it = productItems.iterator(); it.hasNext(); ){
    Product row = (Product)it.next();
%>
    <div class="card m-auto col-2 p-2">
        <div class="image-container"> 
            <img src="/Images/<%=row.getImageUrl()%>" alt="img">
        </div>
        <div class="card-body text-center"> <!-- Center the content horizontally -->
            <div><h3><%= row.getProductName() %></h3></div>
            <div><%= row.getProductDescription() %></div>
            <div><%= row.getCategory() %></div>
            <div><h4>Rupees/- <%= row.getPrice() %></h4></div>
            <div><b>Stock Quantity : <%= row.getStockQuantity() %></b></div> 
        </div>
        <div class="card-footer">
            <form action="remove/product/<%=row.getProductId()%>" method="get" onsubmit="reloadPage()">
                <button type="submit" class="btn btn-danger mb-2" style="width: 100%">Remove Product</button>               
            </form>
            <form action="update/product/<%=row.getProductId()%>" method="get" onsubmit="reloadPage()">
                <button type="submit" class="btn btn-primary mb-2" style="width: 100%">Update Product</button>               
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
