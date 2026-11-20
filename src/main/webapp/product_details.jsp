<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>Add Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-secondary p-2 align-items-center">
    <a href="<%= ctx %>/vendor" class="btn btn-light btn-sm m-2">Home</a>
    <a href="<%= ctx %>/view/all/products" class="btn btn-light btn-sm m-2">All Products</a>
</div>
<div id="topSuccess" class="alert alert-success text-center" role="alert" style="display:none; margin:0; border-radius:0;">
    Product added successfully!
</div>
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center"> 
                <h4 class="mx-auto m-2">Product details</h4>
                <jsp:include page="message_banner.jsp"/>
                <div id="formError" class="alert alert-danger" style="display:none;"></div>
        
        <div class="row">
            <form id="addProductForm" action="<%= ctx %>/product" method="post" autocomplete="off" class="col-md-6 col-md-offset-3" enctype="multipart/form-data">
                <table class="table">
                    <tbody>
                        <tr>
                            <td><label for="productName">Product Name:</label></td>
                            <td><input type="text" placeholder="Enter Product Name" name="productName" class="form-control" id="productName" required></td>
                        </tr>
                        <tr>
                            <td><label for="categorySelect">Category:</label></td>
                            <td>
                                <select id="categorySelect" class="form-control" required>
                                    <option value="">-- Select category --</option>
                                    <option value="womenwear">Women Wear</option>
                                    <option value="menswear">Mens Wear</option>
                                    <option value="kidswear">Kids Wear</option>
                                    <option value="watches">Watches</option>
                                    <option value="electronics">Electronics</option>
                                    <option value="footwear">FootWear</option>
                                    <option value="home">Home</option>
                                    <option value="beauty">Beauty</option>
                                    <option value="sports">Sports</option>
                                    <option value="books">Books</option>
                                    <option value="__other__">Other (type your own)</option>
                                </select>
                                <input type="hidden" name="category" id="category" value="">
                                <input type="text" id="customCategory" class="form-control" placeholder="Enter new category"
                                       style="display:none; margin-top:8px;">
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
                                <input type="file" name="image" class="form-control" id="productImage" required>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="text-center">
                                <button type="reset" class="btn btn-danger">Reset</button>
                                <button type="submit" class="btn btn-success" id="addProductBtn">Add Product</button>
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
    (function () {
        var ctx = "<%= ctx %>";
        var form = document.getElementById("addProductForm");
        var errorBox = document.getElementById("formError");
        var submitBtn = document.getElementById("addProductBtn");
        var categorySelect = document.getElementById("categorySelect");
        var customCategory = document.getElementById("customCategory");
        var categoryHidden = document.getElementById("category");

        function syncCategory() {
            if (categorySelect.value === "__other__") {
                customCategory.style.display = "block";
                customCategory.required = true;
                categoryHidden.value = (customCategory.value || "").trim().toLowerCase().replace(/\s+/g, "");
            } else {
                customCategory.style.display = "none";
                customCategory.required = false;
                customCategory.value = "";
                categoryHidden.value = categorySelect.value;
            }
        }

        categorySelect.addEventListener("change", syncCategory);
        customCategory.addEventListener("input", syncCategory);

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            syncCategory();
            if (!categoryHidden.value) {
                errorBox.textContent = "Please select or enter a category.";
                errorBox.style.display = "block";
                return;
            }
            errorBox.style.display = "none";
            submitBtn.disabled = true;

            fetch(form.action, {
                method: "POST",
                body: new FormData(form),
                credentials: "same-origin",
                redirect: "follow"
            }).then(function (response) {
                if (response.ok || response.redirected) {
                    form.reset();
                    syncCategory();
                    submitBtn.disabled = false;
                    var successBox = document.getElementById("topSuccess");
                    if (successBox) {
                        successBox.style.display = "block";
                        successBox.style.opacity = "1";
                        window.scrollTo({ top: 0, behavior: "smooth" });
                        setTimeout(function () {
                            successBox.style.transition = "opacity 0.4s ease";
                            successBox.style.opacity = "0";
                            setTimeout(function () {
                                successBox.style.display = "none";
                                successBox.style.opacity = "1";
                            }, 400);
                        }, 2000);
                    }
                    return;
                }
                throw new Error("Could not add product. Please try again.");
            }).catch(function (err) {
                if (errorBox) {
                    errorBox.textContent = err.message || "Could not add product. Please try again.";
                    errorBox.style.display = "block";
                }
                if (submitBtn) {
                    submitBtn.disabled = false;
                }
            });
        });
    })();
</script>
</body>
</html>
