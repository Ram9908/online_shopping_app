<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            margin-top: 10%;
        }
    </style>
</head>
<body class=" bg-info">
    <div class="container login-container col-4">
        <div class="card p-4">
            <h1 class="text-center">Login</h1>
            <jsp:include page="message_banner.jsp"/>
            <% if ("1".equals(request.getParameter("signedOut"))) { %>
            <div class="alert alert-success js-auto-hide-alert" role="alert">Signed out successfully.</div>
            <script>
            setTimeout(function () {
                document.querySelectorAll(".js-auto-hide-alert").forEach(function (el) {
                    el.style.transition = "opacity 0.4s ease";
                    el.style.opacity = "0";
                    setTimeout(function () { el.remove(); }, 400);
                });
            }, 2000);
            </script>
            <% } %>
            <form action="login" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" placeholder="Enter email here" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" placeholder="Enter password here" name="password" required>
                </div>
                <div class="mb-3">
                    <label for="role" class="form-label">Role</label>
                    <select class="form-select" id="role" name="role" required>
                        <option value="101">User</option>
                        <option value="102">Vendor</option>
                        <option value="103">Admin</option>
                    </select>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>
            </form>
            <p class="text-center mt-3">Don't have an account? <a href="register.jsp">Sign up here</a></p>
        </div>
    </div>
</body>
</html>
