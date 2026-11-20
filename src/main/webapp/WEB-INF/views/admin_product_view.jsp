<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<%@page import ="com.genpact.online_shopping_app.entity.User"%>
<%! int fontSize; %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>all products</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
 <style>
        /* Your internal CSS styles go here */
        body {
            background-color: #eaf6ff;
            font-family: Arial, sans-serif;
        }
        .card {
        	height:400px;
            border: 1px solid #ccc;
            border-radius: 10px;
            width: 300px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
       
    </style>
</head>
<body>
<jsp:include page="admin_nav.jsp"/>

<h2>All Products</h2>

<div class="d-flex flex-wrap flex-wrap-md gap-3  m-auto p-4 ">
      
<jsp:useBean id="productList" type="java.util.List" scope="request"/>  


<%
  for(Iterator<Product> it = productList.iterator(); it.hasNext(); ){
    Product row = (Product)it.next();
    
%>
				
		    <div class="card m-auto col-2 p-2" >
		        
		            <div class="card-header h-25">Image</div>
		            <div class="card-body ">
		            <div><h3><%= row.getProductName() %></h3></div>
		            <div><%= row.getProductDescription() %></div>
		            <div><%= row.getCategory() %></div>
		            <div><h4>Rupees/- <%= row.getPrice() %></h4></div>
		            <div><b>Stock Quantity : <%= row.getStockQuantity() %></b></div> 
		            </div>
		    </div>
				
<%
  }  // for loop
%>

</div>


</body>
</html>

