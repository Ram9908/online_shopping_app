<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.OrderEntity"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<%@page import ="com.genpact.online_shopping_app.entity.Address"%>
<%@ page import="com.genpact.online_shopping_app.entity.Cart" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Orders</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>

    <div class="container mt-5">
        <h2 class="text-center mb-4">Order List</h2>
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>Order Id</th>
                    <th>Product Image</th>
                     <th>Product Name</th>
               		 <th> Product Description</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>AddressLine1</th>
                    <th>City</th>
                    <th>ZipCode</th>
                	<th>Delete</th>
                </tr>
            </thead>            
            <tbody>            
                <%  List<OrderEntity> updatedOrder= (List<OrderEntity>) request.getAttribute("updatedOrder");
                    if (updatedOrder != null) {
                        for (OrderEntity order : updatedOrder) {                  
                                	List<Product> productList = (List<Product>) request.getAttribute("productList");
                                	for(Product product:productList){
                                	  if(product.getProductId().equals(order.getVirtualProductId())){ 
                                		  List<Address> addressList = (List<Address>) request.getAttribute("addressList");
                                		  for(Address address:addressList){
                                			  if(order.getVirtualAddressId().equals(address.getId())){
                                		  
                %>
                <tr>
                <td><%= order.getOrderId() %></td>
                <td> <img src="/Images/<%=product.getImageUrl()%>" style="max-height:270px;max-width:100%;width:auto;"  alt="img"></td>
                <td><%= product.getProductName() %></td>
                <td><%= product.getProductDescription() %></td>
                <td><%= order.getOrderDate() %></td>
                <td><%= order.getTotalAmount() %></td>
                <td><%= address.getAddressLine1() %></td>
                <td><%= address.getCity() %></td>
                <td><%= address.getZipCode() %></td>
                <td>                                
                     	<form action="delete/<%= order.getOrderId() %>" method="get"  onsubmit="reloadPage()">
                            <input type="hidden" name="orderId" value="<%= order.getOrderId()  %>">
                            <button type="submit" class="btn btn-danger" onclick="showAlert()">Cancel Order</button>
                        </form>
                   </td>
               
            </tr>
                <% 
                                	  }
                                	}
                                }
                          }
                        
                    }
                    }              	
                %>
            </tbody>
        </table>
    </div>
    <script>
        function showAlert() {
            alert("Order Deleted Successfully..");           
        }
        function reloadPage() {
            setTimeout(function() {
                location.reload();
            }, 1000); // Reload after 1 second
        }
    </script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-KyZXEAg3QhqLMpG8r+Knujsl5+06bMQFi4tZ+J8izL4G5xh6AHD66k4JFc+8wewg" crossorigin="anonymous"></script>
</body>
</html>