<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.Product"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Product</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <style>
        body {
            background-color: #e7d9f2;
        }
        .container {
            margin-top: 7%;
            margin-left: 9%;
            margin-right: 2px;
        }
        form {
            border: 2px solid rgb(253, 250, 250);
            border-radius: 10px;
            background-color: #bae2f8;
            padding: 10px;
            margin: 0 auto; 
        }
        .image-container {
            text-align: center; /* Center the image horizontally */
            margin-bottom: 20px; /* Add some space below the image */
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center"> 
                <h4 class="mx-auto m-2">Update Product details</h4>
            </div>
            <div class="row">
                <form action="product/update" class="col-md-6 col-md-offset-3" method="post" onsubmit="reloadPage()">
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label for="productId">Product Id</label>
                            <input type="text" readonly placeholder="${product.productId}" name="productId" class="form-control" value="${product.productId}" required>
                        </div> 
                        <div class="col-md-6 form-group">
                            <label for="productName">Product Name</label>
                            <input type="text" readonly placeholder="${product.productName}" name="productName" class="form-control" value="${product.productName}" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="image-container"> <!-- Container for the product image -->
                            <img src="/Images/${product.imageUrl}" style="max-height:100px;max-width:30%;width:50%;" alt="Product Image">
                        </div>
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="category">Category</label>
                        <input type="text" readonly placeholder="${product.category}" name="category" class="form-control" value="${product.category}" required>
                    </div>
                    <div class="col-md-6 form-group">
                        <label for="productDescription">Product Description</label>
                        <textarea name="productDescription" class="form-control" id="productDescription" required>${product.productDescription}</textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label for="price">Price</label>
                            <input type="number" placeholder="${product.price}" name="price" class="form-control" id="price" value="${product.price}" required>
                        </div>
                        <div class="col-md-6 form-group">
                            <label for="stockQuantity">Stock Quantity</label>
                            <input type="number" placeholder="${product.stockQuantity}" name="stockQuantity" class="form-control" id="stockQuantity" value="${product.stockQuantity}" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-center" style="margin-bottom: 2px;">
                            <button type="reset" class="btn btn-danger">Reset</button>
                        </div>
                        <div class="col-md-6 text-center">
                            <button type="submit" class="btn btn-success">Update Product</button>
                        </div>
                    </div>
                </form>
            </div>
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
