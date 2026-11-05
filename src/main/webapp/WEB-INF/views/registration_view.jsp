<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Success</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%-- Check if success message is present --%>
    <% if (request.getAttribute("successMessage") != null) { %>
        <div id="alertMessage" class="alert alert-success" role="alert">${successMessage}</div>
        
        <script>
            // Hide alert message after 3 seconds
            setTimeout(function() {
                var alertMessage = document.getElementById('alertMessage');
                alertMessage.style.display = 'none';
            }, 3000);
        </script>
    
    <% } %>
    <%-- Your other HTML content here --%>
</body>
</html>
