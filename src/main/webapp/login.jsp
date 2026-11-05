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
            <form action="login" method="post" onsubmit="reloadPage()">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" placeholder="Enter email here" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" placeholder="Enter password here" name="password" pattern="(?=.*[A-Z])[a-zA-Z0-9]{8,}" title="Password must be at least 8 characters long, contain only alphabets and digits, and include at least one capital letter" required>
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

    <script>
        function reloadPage() {
            setTimeout(function() {
                location.reload();
            }, 1000); // Reload after 1 second
        }
    </script>
</body>
</html>