<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Address Form</title>
</head>
<body>
    <h2>Address Form</h2>
    <form action="order/submitAddress" method="post" onsubmit="reloadPage()">
        <label for="name">Name:</label>
        <input type="text" name="name" required><br><br>
        
        <label for="addressLine1">Address Line 1:</label>
        <input type="text" id="addressLine1" name="addressLine1" required><br><br>
        
        <label for="addressLine2">Address Line 2:</label>
        <input type="text" id="addressLine2" name="addressLine2" ><br><br>
        
        <label for="city">City:</label>
        <input type="text" id="city" name="city" required><br><br>
        
        <label for="state">State:</label>
        <input type="text" id="state" name="state" required><br><br>
        
        <label for="zipCode">Zip Code:</label>
        <input type="text" id="zipCode" name="zipCode" required><br><br>
        
        
        <input type="submit" value="Submit" onclick="showAlert()">
    </form>
    <script>
        function showAlert() {
            alert("Order Placed Successfully..");           
        }
        function reloadPage() {
            setTimeout(function() {
                location.reload();
            }, 1000); // Reload after 1 second
        }
    </script>
</body>
</html>
