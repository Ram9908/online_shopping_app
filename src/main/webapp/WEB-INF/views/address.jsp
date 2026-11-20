<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Address Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #eaf6ff;
            font-family: Arial, sans-serif;
        }
        .address-card {
            max-width: 560px;
            margin: 2.5rem auto;
            background: #fff;
            border: 1px solid #d7e3ef;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
            padding: 1.5rem 1.75rem;
        }
        .address-card h2 {
            text-align: center;
            margin-bottom: 1.25rem;
        }
        .form-actions {
            display: flex;
            gap: 0.75rem;
            justify-content: center;
            margin-top: 0.5rem;
        }
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-secondary p-2 justify-content-between align-items-center">
    <a href="<%= ctx %>/user" class="btn btn-light btn-sm m-2">Home</a>
    <a href="<%= ctx %>/view/all/cart" class="btn btn-light btn-sm m-2">Cart</a>
</div>
<jsp:include page="message_banner.jsp"/>

<div class="address-card">
    <h2>Delivery Address</h2>
    <form action="<%= ctx %>/view/all/add/order/submitAddress" method="post">
        <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Full name" required>
        </div>
        <div class="mb-3">
            <label for="addressLine1" class="form-label">Address Line 1</label>
            <input type="text" class="form-control" id="addressLine1" name="addressLine1"
                   placeholder="House no., street" required>
        </div>
        <div class="mb-3">
            <label for="addressLine2" class="form-label">Address Line 2</label>
            <input type="text" class="form-control" id="addressLine2" name="addressLine2"
                   placeholder="Landmark (optional)">
        </div>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="city" class="form-label">City</label>
                <input type="text" class="form-control" id="city" name="city" required>
            </div>
            <div class="col-md-6 mb-3">
                <label for="state" class="form-label">State</label>
                <input type="text" class="form-control" id="state" name="state" required>
            </div>
        </div>
        <div class="mb-3">
            <label for="zipCode" class="form-label">Zip Code</label>
            <input type="text" class="form-control" id="zipCode" name="zipCode" required>
        </div>
        <div class="form-actions">
            <a href="<%= ctx %>/view/all/cart" class="btn btn-secondary">Back to Cart</a>
            <button type="submit" class="btn btn-success">Place Order</button>
        </div>
    </form>
</div>
</body>
</html>
