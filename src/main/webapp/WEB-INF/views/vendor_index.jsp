<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Vendor Index</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href="Index-Style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</head>
<body>
    <div class="d-flex bg-info p-1 justify-content-between">
        <div class="d-flex">
            <div class="m-2"> 
                <img style="border-radius: 60px; height: 40px; width: 40px" src="https://t4.ftcdn.net/jpg/06/22/16/09/240_F_622160930_eg05Gu6D58YApICIw9q8ZyuLsVQlcYmP.jpg">
            	<select class="bg-warning p-1 rounded text-light" name="All Categories" onchange="window.location.href=this.value">
            		<option value="view/all/products">All Categories</option>
	                <option value="/p/category/womenwear">Womens Wear</option> 
	                <option value="/p/category/menswear">Mens Wear</option>  
	                <option value="/p/category/kidswear">Kids Wear</option> 
	                <option value="/p/category/watches">Watches</option> 
	                <option value="/p/category/electronics">Electronics</option> 
	                <option value="/p/category/footwear">FootWear</option> 
                </select>
            </div>
        </div>

        
    </div>

    <div class="d-flex bg-secondary p-2 justify-content-between text-light">
        <div class="m-2 text-light">
            <span class="m-2">Home</span>
        </div>
        <div>
            <form class="forms" action="/p/{productName}" method="get">
        		<input type="search" name="productName" placeholder="Search here"/>
    		</form>
        </div>
        <div class="d-flex">
            <div class="d-flex">
	            <span class="m-2 border-colour-dark"><a href="view/all/products" class="text-decoration-none text-light mx-3">Product view</a></span>
	            <span class="m-2 border-colour-dark"><a href="product_details.jsp" class="text-decoration-none text-light mx-3">Product</a></span>
	        </div>
        </div>
    </div>

    <div class="back bg-gradient-primary d-flex">
        <div class="m">
            <h2>Up to 75% off</h2>
            <h1><bd>Fashion Collection Summer Sale</bd></h1>
        </div>
        <div class="n">
            <img style="border-radius: 300px;" src="https://images.pexels.com/photos/4127629/pexels-photo-4127629.jpeg?auto=compress&cs=tinysrgb&w=600"/>
        </div>
    </div>
    <div class="min-body" >
        <div class = "imaget">
           <img src = "https://shorturl.at/gvJLP"/>
        </div>
        <div class="text-content">
        <h3>Welcome to Our eCommerce Platform</h3>
        <p>
            At XYZ E-commerce, we specialize in providing an exceptional online shopping experience for our customers. Here are some of our key specialties:
        </p>
 
        <h4>Exclusive Deals and Discounts</h4>
        <p>
            We regularly feature exclusive deals and discounts on popular products, allowing our customers to enjoy significant savings on their purchases. 
            Keep an eye out for our special promotions and limited-time offers!
        </p>
    
    </div>
    </div>

    <footer>
        <div class="d-flex bg-secondary justify-content-evenly text-light">
            <div>
                <ul>
                    <li><b> Get to Know Us</b></li>
                    <li> About Us</li>
                    <li> Careers</li>
                    <li> Press Releases</li>
                
                </ul>
            </div>
            <div>
                <ul>
                    <li><b> Connect with Us</b></li>
                    <li>Facebook </li>
                    <li> Twitter</li>
                    <li> Instagram</li>
                </ul>
            </div>
            <div>
                <ul>	
                    <li><b>Make Money with Us </b></li>
                    <li>Sell on App </li>
                    <li> Sell under App Accelerator</li>
                    <li> Protect and Build Your Brand</li>
                </ul>
            </div>
            <div>
                <ul>
                    <li> <b>Let Us Help You</b></li>
                    <li> Your Account</li>
                    <li> Returns Centre</li>
                </ul>
            </div>
        </div>
    </footer>
</body>
</html>