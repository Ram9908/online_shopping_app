<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div class="common-div">
<div class="d-flex bg-info p-1 justify-content-between">
        <div class="d-flex">
            <div class="m-2"> 
                <img style="border-radius: 60px; height: 40px; width: 40px" src="https://t4.ftcdn.net/jpg/06/22/16/09/240_F_622160930_eg05Gu6D58YApICIw9q8ZyuLsVQlcYmP.jpg">
            	<select class="bg-warning p-1 rounded text-light" name="All Categories" onchange="window.location.href=this.value">
            		<option value="view/all/products">All Categories</option>
	                <option value="/products/category/womenwear">Womens Wear</option> 
	                <option value="/products/category/menswear">Mens Wear</option>  
	                <option value="/products/category/kidswear">Kids Wear</option> 
	                <option value="/products/category/watches">Watches</option> 
	                <option value="/products/category/electronics">Electronics</option> 
	                <option value="/products/category/footwear">FootWear</option> 
                </select>
            </div>
        </div>

        <div class="d-flex">
            <div class="m-2">
                <img style="border-radius: 3px; height: 25px; width: 25px;" src="https://cdn-icons-png.flaticon.com/128/2089/2089548.png"/>
                <span class="m-2 border-colour-dark"><a href="/ordersView" class="text-decoration-none text-dark">Track Order</a></span>
            </div>
           
            <div class="m-2">
                <img style="border-radius: 3px; height: 25px; width: 25px;" src="https://cdn-icons-png.flaticon.com/128/2838/2838838.png"/>
                <span><a href="view/all/cart" class="text-decoration-none text-dark">cart</a></span>
            </div>
            
        </div>
    </div>

    <div class="d-flex bg-secondary p-2 justify-content-between text-light">
        <div class="m-2 text-light">
            <span class="m-2">Home</span>
        </div>
      
        <div class="d-flex">
        	
            <span class="m-2 border-colour-dark"><a href="view/all" class="text-decoration-none text-light mx-3">view products</a></span>           
        </div>
    </div>
</div>
</body>
</html>