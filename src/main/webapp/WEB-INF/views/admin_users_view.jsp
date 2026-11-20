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
    <style>
        body { background-color: #eaf6ff; font-family: Arial, sans-serif; }
        .btn-delete-user {
            padding: 0.2rem 0.55rem;
            font-size: 0.8rem;
            border-radius: 6px;
        }
        .delete-modal-backdrop {
            display: none;
            position: fixed;
            inset: 0;
            background: rgba(15, 23, 42, 0.55);
            z-index: 1050;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }
        .delete-modal-backdrop.show { display: flex; }
        .delete-modal {
            background: #fff;
            border-radius: 12px;
            max-width: 420px;
            width: 100%;
            padding: 1.4rem 1.5rem 1.25rem;
            box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        .delete-modal h5 { margin: 0 0 0.5rem; color: #b42318; }
        .delete-modal .name { font-weight: 600; margin-bottom: 0.75rem; }
        .delete-modal .hint { font-size: 0.9rem; color: #64748b; margin-bottom: 0.85rem; }
        .delete-modal { text-align: left; }
        .delete-option { display: block; margin-bottom: 0.45rem; font-size: 0.92rem; }
        .delete-actions { display: flex; justify-content: flex-end; gap: 0.5rem; margin-top: 1rem; }
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
    <jsp:include page="admin_nav.jsp"/>
    <div id="topSuccess" class="alert alert-success text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>
    <div id="topError" class="alert alert-danger text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>
    <div class="container mt-5">
        <h2 class="text-center text-primary">Admin - Users</h2>
        <div class="table-responsive bg-white rounded shadow-sm p-2">
        <table class="table table-bordered table-striped align-middle mb-0">
            <thead class="table-dark">
                <tr>
                    <th>User Id</th>
                    <th>User Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<User> userList = (List<User>) request.getAttribute("userList");
                    if (userList != null) {
                        for (User user : userList) {
                %>
                <tr data-row-id="<%= user.getUserId() %>">
                    <td><%= user.getUserId() %></td>
                    <td><%= user.getUserName() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getPhone() %></td>
                    <td>
                        <button type="button" class="btn btn-outline-danger btn-delete-user delete-btn"
                                data-id="<%= user.getUserId() %>"
                                data-name="<%= user.getUserName() == null ? "" : user.getUserName().replace("\"", "&quot;") %>">
                            Delete
                        </button>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
        </div>
    </div>

<div id="deleteModal" class="delete-modal-backdrop" role="dialog" aria-modal="true">
    <div class="delete-modal">
        <h5 class="text-center">Why delete this user?</h5>
        <p class="name text-center" id="deleteName"></p>
        <p class="hint">Select a reason before confirming:</p>
        <form id="deleteReasonForm">
            <label class="delete-option">
                <input type="radio" name="reason" value="Fraudulent activity" required>
                Fraudulent activity
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Abusive or harassing behavior" required>
                Abusive or harassing behavior
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Fake or duplicate account" required>
                Fake or duplicate account
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Payment misuse or chargeback abuse" required>
                Payment misuse or chargeback abuse
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Spam or suspicious orders" required>
                Spam or suspicious orders
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Violation of terms of service" required>
                Violation of terms of service
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Other unethical behavior" required>
                Other unethical behavior
            </label>
            <div class="delete-actions">
                <button type="button" class="btn btn-secondary btn-sm" onclick="closeDeleteModal()">Keep User</button>
                <button type="submit" id="confirmDeleteBtn" class="btn btn-danger btn-sm">Confirm Delete</button>
            </div>
        </form>
    </div>
</div>

<script>
    var ctx = "<%= ctx %>";
    var pendingId = null;
    function showBanner(id, message) {
        var box = document.getElementById(id);
        if (!box) return;
        box.textContent = message || "";
        box.style.display = "block";
        box.style.opacity = "1";
        window.scrollTo({ top: 0, behavior: "smooth" });
        setTimeout(function () {
            box.style.transition = "opacity 0.4s ease";
            box.style.opacity = "0";
            setTimeout(function () {
                box.style.display = "none";
                box.style.opacity = "1";
            }, 400);
        }, 2000);
    }
    function openDeleteModal(id, name) {
        pendingId = id;
        document.getElementById("deleteName").textContent = name || "this user";
        document.querySelectorAll("#deleteReasonForm input[name='reason']").forEach(function (r) {
            r.checked = false;
        });
        document.getElementById("deleteModal").classList.add("show");
    }
    function closeDeleteModal() {
        pendingId = null;
        document.getElementById("deleteModal").classList.remove("show");
    }
    document.querySelectorAll(".delete-btn").forEach(function (btn) {
        btn.addEventListener("click", function () {
            openDeleteModal(btn.getAttribute("data-id"), btn.getAttribute("data-name"));
        });
    });
    document.getElementById("deleteReasonForm").addEventListener("submit", function (event) {
        event.preventDefault();
        if (!pendingId) return;
        var selected = document.querySelector("#deleteReasonForm input[name='reason']:checked");
        if (!selected) {
            showBanner("topError", "Please select a reason.");
            return;
        }
        var id = pendingId;
        var reason = selected.value;
        var btn = document.getElementById("confirmDeleteBtn");
        btn.disabled = true;
        fetch(ctx + "/add/user/remove/" + encodeURIComponent(id) + "?reason=" + encodeURIComponent(reason), {
            method: "POST",
            credentials: "same-origin",
            headers: { "Accept": "application/json" }
        }).then(function (response) {
            return response.json().then(function (data) {
                return { ok: response.ok, data: data || {} };
            }).catch(function () {
                return { ok: false, data: { message: "Could not delete user." } };
            });
        }).then(function (result) {
            btn.disabled = false;
            closeDeleteModal();
            if (result.ok && result.data.success) {
                var row = document.querySelector('[data-row-id="' + id + '"]');
                if (row) row.remove();
                showBanner("topSuccess", result.data.message || "User deleted successfully.");
                return;
            }
            showBanner("topError", (result.data && result.data.message) || "Could not delete user.");
        }).catch(function (err) {
            btn.disabled = false;
            closeDeleteModal();
            showBanner("topError", err.message || "Could not delete user.");
        });
    });
    document.getElementById("deleteModal").addEventListener("click", function (e) {
        if (e.target === this) closeDeleteModal();
    });
</script>
</body>
</html>
