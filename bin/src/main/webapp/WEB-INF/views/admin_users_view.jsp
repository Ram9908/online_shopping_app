<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Users</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="admin_nav.jsp"/>
    <div class="container mt-5">
        <h2 class="text-center text-primary">Admin - Users</h2>
        <table class="table table-bordered table-striped">
            <thead class="table-primary ">
                <tr class="bg-dark-violet text-white">
                    <th>User Id</th>
                    <th>User Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<User> userList = (List<User>) request.getAttribute("userList");
                    if (userList != null) {
                        for (User user : userList) {
                %>
                <tr>
                    <td><%= user.getUserId() %></td>
                    <td><%= user.getUserName() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getPhone() %></td>                    
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