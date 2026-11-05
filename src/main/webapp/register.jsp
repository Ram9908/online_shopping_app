<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .registration-container {
            margin-top: 5.5%;
        }
    </style>
</head>
<body class=" bg-info">
    <div class="container registration-container col-4 mt-10">
        <div class="card p-4">
            <h1 class="text-center">Registration Form</h1>
            <form id="registrationForm" action="signup" method="post" onsubmit="reloadPage()">
                <div class="mb-3">
                    <label for="name" class="form-label">Name:</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone:</label>
                    <input type="text" class="form-control" id="phone" name="phone" pattern="[0-9]{10}" title="Phone number should be 10 digits" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" pattern="(?=.*[A-Z])[a-zA-Z0-9]{8,}" title="Password must be at least 8 characters long, contain only alphabets and digits, and include at least one capital letter" required>
                </div>
                <div class="mb-3">
                    <label for="role" class="form-label">Role:</label>
                    <select class="form-select" name="role" required>
                        <option value="User">User</option>
                        <option value="Vendor">Vendor</option>
                    </select>
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">Register</button>
                </div>
            </form>
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
