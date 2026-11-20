<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.Product"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Product</title>
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
        .image-container {
            text-align: center;
            margin: 10px 0 16px;
            background: #f3f7fb;
            border-radius: 8px;
            padding: 10px;
            height: 180px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .image-container img {
            max-width: 100%;
            max-height: 100%;
            width: auto;
            height: auto;
            object-fit: contain;
        }
        #productDescription {
            min-height: 100px;
            resize: vertical;
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
    Product updated successfully!
</div>
<jsp:include page="message_banner.jsp"/>
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center">
                <h4 class="mx-auto m-2">Update Product details</h4>
                <div id="formError" class="alert alert-danger" style="display:none;"></div>
            </div>
            <div class="row">
                <form id="updateProductForm" action="<%= ctx %>/view/all/update/product/product/update"
                      class="col-md-6 col-md-offset-3" method="post">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <table class="table">
                        <tbody>
                            <tr>
                                <td><label for="productName">Product Name:</label></td>
                                <td>
                                    <input type="text" name="productName" class="form-control"
                                           value="${product.productName}" required>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="image-container">
                                        <img src="/Images/${product.imageUrl}" alt="Product Image">
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="categorySelect">Category:</label></td>
                                <td>
                                    <select id="categorySelect" class="form-control" required>
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
                                    <input type="hidden" name="category" id="category" value="${product.category}">
                                    <input type="text" id="customCategory" class="form-control"
                                           placeholder="Enter new category"
                                           style="display:none; margin-top:8px;">
                                </td>
                            </tr>
                            <tr>
                                <td><label for="productDescription">Product Description:</label></td>
                                <td>
                                    <textarea name="productDescription" class="form-control"
                                              id="productDescription" required>${product.productDescription}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="price">Price:</label></td>
                                <td>
                                    <input type="number" name="price" class="form-control" id="price"
                                           value="${product.price}" required>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="stockQuantity">Stock Quantity:</label></td>
                                <td>
                                    <input type="number" name="stockQuantity" class="form-control" id="stockQuantity"
                                           value="${product.stockQuantity}" required>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="text-center">
                                    <button type="button" class="btn btn-danger" id="resetProductBtn">Reset</button>
                                    <button type="submit" class="btn btn-success" id="updateProductBtn">Update Product</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
    <script>
        (function () {
            var form = document.getElementById("updateProductForm");
            var select = document.getElementById("categorySelect");
            var custom = document.getElementById("customCategory");
            var hidden = document.getElementById("category");
            var errorBox = document.getElementById("formError");
            var submitBtn = document.getElementById("updateProductBtn");
            var original = {
                productName: form.productName.value,
                productDescription: form.productDescription.value,
                price: form.price.value,
                stockQuantity: form.stockQuantity.value,
                category: (hidden.value || "").trim()
            };

            function applyCategory(value) {
                var matched = false;
                for (var i = 0; i < select.options.length; i++) {
                    if (select.options[i].value === value) {
                        select.value = value;
                        matched = true;
                        break;
                    }
                }
                if (!matched && value) {
                    select.value = "__other__";
                    custom.style.display = "block";
                    custom.value = value;
                    custom.required = true;
                    hidden.value = value;
                } else {
                    custom.style.display = "none";
                    custom.required = false;
                    custom.value = "";
                    hidden.value = select.value;
                }
            }

            function sync() {
                if (select.value === "__other__") {
                    custom.style.display = "block";
                    custom.required = true;
                    hidden.value = (custom.value || "").trim().toLowerCase().replace(/\s+/g, "");
                } else {
                    custom.style.display = "none";
                    custom.required = false;
                    hidden.value = select.value;
                }
            }

            function showSuccess() {
                var successBox = document.getElementById("topSuccess");
                if (!successBox) {
                    return;
                }
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

            applyCategory(original.category);

            select.addEventListener("change", sync);
            custom.addEventListener("input", sync);

            form.addEventListener("submit", function (event) {
                event.preventDefault();
                sync();
                if (!hidden.value) {
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
                    submitBtn.disabled = false;
                    if (response.ok || response.redirected) {
                        original.productName = form.productName.value;
                        original.productDescription = form.productDescription.value;
                        original.price = form.price.value;
                        original.stockQuantity = form.stockQuantity.value;
                        original.category = (hidden.value || "").trim();
                        showSuccess();
                        return;
                    }
                    throw new Error("Could not update product. Please try again.");
                }).catch(function (err) {
                    errorBox.textContent = err.message || "Could not update product. Please try again.";
                    errorBox.style.display = "block";
                    submitBtn.disabled = false;
                });
            });

            document.getElementById("resetProductBtn").addEventListener("click", function () {
                form.productName.value = original.productName;
                form.productDescription.value = original.productDescription;
                form.price.value = original.price;
                form.stockQuantity.value = original.stockQuantity;
                applyCategory(original.category);
            });
        })();
    </script>
</body>
</html>
