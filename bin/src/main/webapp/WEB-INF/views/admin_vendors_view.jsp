<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.Vendor"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Admin - Vendors</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding: 20px;
        }
        h2 {
            color: #6c757d; /* Bootstrap dark color */
            margin-bottom: 20px;
        }
        .bg-dark-violet {
            background-color: #4d2c49; /* Dark violet color */
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center text-primary">Admin - Vendors</h2>
        <table class="table table-bordered table-striped">
            <thead class="table-primary bg-primary">
                <tr class="text-white">
                    <th>Vendor Id</th>
                    <th>Vendor Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Vendor> vendorList = (List<Vendor>) request.getAttribute("vendorList");
                    if (vendorList != null) {
                        for (Vendor vendor : vendorList) {
                %>
                <tr>
                    <td><%= vendor.getVendorId() %></td>
                    <td><%= vendor.getVendorName() %></td>
                    <td><%= vendor.getEmail() %></td>
                    <td><%= vendor.getPhone() %></td>                    
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
    
</body>
</html>