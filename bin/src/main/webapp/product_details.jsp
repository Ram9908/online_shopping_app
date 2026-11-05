<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Product</title>
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
        .preview-image {
            width: 100px;
            height: 100px;
            border: 1px solid #ccc;
            margin-top: 10px;
            display: none; /* hide by default */
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center"> 
                <h4 class="mx-auto m-2">Product details</h4>
        
        <div class="row">
            <form action="product" method="post" onsubmit="reloadPage()" class="col-md-6 col-md-offset-3" enctype="multipart/form-data">
                <table class="table">
                    <tbody>
                        <tr>
                            <td><label for="productName">Product Name:</label></td>
                            <td><input type="text" placeholder="Enter Product Name" name="productName" class="form-control" id="productName" required></td>
                        </tr>
                        <tr>
                            <td><label for="category">Category:</label></td>
                            <td>
                                <select name="category" id="category" class="form-control" required>
                                    <option value="womenwear">Women Wear</option>
                                    <option value="menswear">Mens Wear</option>
                                    <option value="kidswear">Kids Wear</option>
                                    <option value="watch">Watches</option>
                                    <option value="electronics">Electronics</option>
                                    <option value="footwear">FootWear</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="productDescription">Product Description:</label></td>
                            <td><textarea name="productDescription" class="form-control" id="productDescription" required></textarea></td>
                        </tr>
                        <tr>
                            <td><label for="price">Price:</label></td>
                            <td><input type="number" placeholder="Enter Unit Price" name="price" class="form-control" id="price" required></td>
                        </tr>
                        <tr>
                            <td><label for="stockQuantity">Stock Quantity:</label></td>
                            <td><input type="number" placeholder="Enter Stock Quantity" name="stockQuantity" class="form-control" id="stockQuantity" required></td>
                        </tr>
                        <tr>
                            <td><label for="productImage">Product Image:</label></td>
                            <td>
                                <input type="file" name="image" class="form-control"  required>
                                
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="text-center">
                                <button type="reset" class="btn btn-danger">Reset</button>
                                <button type="submit" class="btn btn-success">Add Product</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    
</div>
</div>
<script>
    // Function to reload the page after successfully submitting the product
    function reloadPage() {
        setTimeout(function() {
            location.reload();
        }, 1000); // Reload after 1 second
    }
</script>
    

</body>
</html>
