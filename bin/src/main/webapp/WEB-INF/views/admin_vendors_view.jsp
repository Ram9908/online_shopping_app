<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import="com.genpact.online_shopping_app.entity.Vendor"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Vendors</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #eaf6ff; font-family: Arial, sans-serif; }
        .btn-delete-vendor {
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
            text-align: left;
        }
        .delete-modal h5 { margin: 0 0 0.5rem; color: #b42318; text-align: center; }
        .delete-modal .name { font-weight: 600; margin-bottom: 0.75rem; text-align: center; }
        .delete-modal .hint { font-size: 0.9rem; color: #64748b; margin-bottom: 0.85rem; }
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
    <div id="topWarning" class="alert alert-warning text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>
    <div id="topError" class="alert alert-danger text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>
    <div class="container mt-5">
        <h2 class="text-center text-primary">Admin - Vendors</h2>
        <div class="table-responsive bg-white rounded shadow-sm p-2">
        <table class="table table-bordered table-striped align-middle mb-0">
            <thead class="table-dark">
                <tr>
                    <th>Vendor Id</th>
                    <th>Vendor Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Vendor> vendorList = (List<Vendor>) request.getAttribute("vendorList");
                    if (vendorList != null) {
                        for (Vendor vendor : vendorList) {
                        	boolean warned = vendor.getWarningCount() >= 1;
                %>
                <tr data-row-id="<%= vendor.getVendorId() %>" data-warned="<%= warned %>">
                    <td><%= vendor.getVendorId() %></td>
                    <td><%= vendor.getVendorName() %></td>
                    <td><%= vendor.getEmail() %></td>
                    <td><%= vendor.getPhone() %></td>
                    <td class="status-cell">
                        <% if (warned) { %>
                        <span class="badge text-bg-warning">Warned</span>
                        <% } else { %>
                        <span class="badge text-bg-success">Active</span>
                        <% } %>
                    </td>
                    <td>
                        <button type="button"
                                class="btn <%= warned ? "btn-outline-danger" : "btn-outline-warning" %> btn-delete-vendor delete-btn"
                                data-id="<%= vendor.getVendorId() %>"
                                data-warned="<%= warned %>"
                                data-name="<%= vendor.getVendorName() == null ? "" : vendor.getVendorName().replace("\"", "&quot;") %>">
                            <%= warned ? "Delete" : "Warn" %>
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
        <h5 id="modalTitle">Why take action?</h5>
        <p class="name" id="deleteName"></p>
        <p class="hint" id="modalHint">Select a reason:</p>
        <form id="deleteReasonForm">
            <label class="delete-option">
                <input type="radio" name="reason" value="Selling fake or prohibited products" required>
                Selling fake or prohibited products
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Fraudulent or misleading listings" required>
                Fraudulent or misleading listings
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Repeated customer complaints" required>
                Repeated customer complaints
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Policy or terms violation" required>
                Policy or terms violation
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Abusive or unethical behavior" required>
                Abusive or unethical behavior
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Inactive or abandoned account" required>
                Inactive or abandoned account
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Other unethical activity" required>
                Other unethical activity
            </label>
            <div class="delete-actions">
                <button type="button" class="btn btn-secondary btn-sm" onclick="closeDeleteModal()">Cancel</button>
                <button type="submit" id="confirmDeleteBtn" class="btn btn-danger btn-sm">Confirm</button>
            </div>
        </form>
    </div>
</div>

<script>
    var ctx = "<%= ctx %>";
    var pendingId = null;
    var pendingWarned = false;
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
        }, 2500);
    }
    function openDeleteModal(id, name, warned) {
        pendingId = id;
        pendingWarned = warned === true || warned === "true";
        document.getElementById("deleteName").textContent = name || "this vendor";
        document.getElementById("modalTitle").textContent = pendingWarned
            ? "Why delete this vendor?"
            : "Why warn this vendor?";
        document.getElementById("modalHint").textContent = pendingWarned
            ? "This vendor was already warned. Selecting a reason will permanently delete them."
            : "First chance: issue a warning only. They will not be deleted yet.";
        document.getElementById("confirmDeleteBtn").textContent = pendingWarned ? "Confirm Delete" : "Send Warning";
        document.getElementById("confirmDeleteBtn").className = pendingWarned
            ? "btn btn-danger btn-sm"
            : "btn btn-warning btn-sm";
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
            openDeleteModal(
                btn.getAttribute("data-id"),
                btn.getAttribute("data-name"),
                btn.getAttribute("data-warned")
            );
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
        fetch(ctx + "/add/vendor/remove/" + encodeURIComponent(id) + "?reason=" + encodeURIComponent(reason), {
            method: "POST",
            credentials: "same-origin",
            headers: { "Accept": "application/json" }
        }).then(function (response) {
            return response.json().then(function (data) {
                return { ok: response.ok, data: data || {} };
            }).catch(function () {
                return { ok: false, data: { message: "Could not process vendor action." } };
            });
        }).then(function (result) {
            btn.disabled = false;
            closeDeleteModal();
            if (result.ok && result.data.success) {
                if (result.data.deleted) {
                    var row = document.querySelector('[data-row-id="' + id + '"]');
                    if (row) row.remove();
                    showBanner("topSuccess", result.data.message || "Vendor deleted.");
                    return;
                }
                if (result.data.warned) {
                    var warnedRow = document.querySelector('[data-row-id="' + id + '"]');
                    if (warnedRow) {
                        warnedRow.setAttribute("data-warned", "true");
                        var statusCell = warnedRow.querySelector(".status-cell");
                        if (statusCell) {
                            statusCell.innerHTML = '<span class="badge text-bg-warning">Warned</span>';
                        }
                        var actionBtn = warnedRow.querySelector(".delete-btn");
                        if (actionBtn) {
                            actionBtn.setAttribute("data-warned", "true");
                            actionBtn.textContent = "Delete";
                            actionBtn.className = "btn btn-outline-danger btn-delete-vendor delete-btn";
                        }
                    }
                    showBanner("topWarning", result.data.message || "Warning issued to vendor.");
                    return;
                }
                showBanner("topSuccess", result.data.message || "Done.");
                return;
            }
            showBanner("topError", (result.data && result.data.message) || "Could not process vendor action.");
        }).catch(function (err) {
            btn.disabled = false;
            closeDeleteModal();
            showBanner("topError", err.message || "Could not process vendor action.");
        });
    });
    document.getElementById("deleteModal").addEventListener("click", function (e) {
        if (e.target === this) closeDeleteModal();
    });
</script>
</body>
</html>
