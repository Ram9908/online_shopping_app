<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>All Products</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
 <style>
        body {
            background-color: #eaf6ff;
            font-family: Arial, sans-serif;
        }
        .product-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            align-items: stretch;
        }
        .card {
            border: 1px solid #ccc;
            border-radius: 10px;
            width: 280px;
            height: 560px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            overflow: hidden;
            background: #fff;
        }
        .image-container {
            height: 180px;
            overflow: hidden;
            text-align: center;
            flex-shrink: 0;
            background: #f3f7fb;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .image-container img {
            max-width: 100%;
            max-height: 100%;
            width: auto;
            height: auto;
            object-fit: contain;
        }
        .card-body {
            flex: 1 1 auto;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            min-height: 0;
        }
        .card-body h3 {
            font-size: 1.25rem;
            margin: 0.5rem 0;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            flex-shrink: 0;
        }
        .card-desc-wrap {
            height: 88px;
            margin-bottom: 0.35rem;
            flex-shrink: 0;
        }
        .card-desc {
            height: 100%;
            overflow-x: hidden;
            overflow-y: auto;
            text-align: left;
            padding: 0.4rem 0.55rem;
            border: 1px solid #e0e6ed;
            border-radius: 6px;
            background: #fafcfe;
            -webkit-overflow-scrolling: touch;
            scrollbar-width: thin;
            overscroll-behavior: contain;
            cursor: grab;
            user-select: none;
            touch-action: pan-y;
        }
        .card-desc:active {
            cursor: grabbing;
        }
        .card-desc-inner {
            display: block;
            white-space: normal;
            line-height: 1.4;
            font-size: 0.9rem;
            word-break: break-word;
        }
        .card-meta {
            flex-shrink: 0;
        }
        .card-footer {
            margin-top: auto;
            flex-shrink: 0;
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
        .delete-modal-backdrop.show {
            display: flex;
        }
        .delete-modal {
            background: #fff;
            border-radius: 12px;
            max-width: 420px;
            width: 100%;
            padding: 1.4rem 1.5rem 1.25rem;
            box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        .delete-modal h5 {
            margin: 0 0 0.5rem;
            color: #b42318;
            text-align: center;
        }
        .delete-modal p {
            margin: 0 0 0.35rem;
            color: #334155;
        }
        .delete-modal .product-name {
            font-weight: 600;
            color: #0f172a;
            margin-bottom: 0.75rem;
            text-align: center;
        }
        .delete-modal .hint {
            font-size: 0.9rem;
            color: #64748b;
            margin-bottom: 0.85rem;
            text-align: left;
        }
        .delete-modal {
            text-align: left;
        }
        .delete-option {
            display: block;
            margin-bottom: 0.45rem;
            font-size: 0.92rem;
        }
        .delete-actions {
            display: flex;
            justify-content: flex-end;
            gap: 0.5rem;
            margin-top: 1rem;
        }
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="admin_nav.jsp"/>
<div id="topSuccess" class="alert alert-success text-center" role="alert" style="display:none; margin:0; border-radius:0;">
    Product deleted successfully!
</div>
<div id="topError" class="alert alert-danger text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>

<h2 class="m-3">All Products</h2>

<div class="product-grid m-auto p-4">
      
<jsp:useBean id="productList" type="java.util.List" scope="request"/>  

<%
  for(Iterator<Product> it = productList.iterator(); it.hasNext(); ){
    Product row = (Product)it.next();
%>
    <div class="card p-2" data-product-card="<%= row.getProductId() %>">
        <div class="image-container">
            <img src="/Images/<%=row.getImageUrl()%>" alt="img">
        </div>
        <div class="card-body text-center">
            <div><h3 title="<%= row.getProductName() %>"><%= row.getProductName() %></h3></div>
            <div class="card-desc-wrap">
                <div class="card-desc" title="Swipe or scroll to read full description">
                    <span class="card-desc-inner"><%= row.getProductDescription() %></span>
                </div>
            </div>
            <div class="card-meta">
                <div><%= row.getCategory() %></div>
                <div><h4>Rupees/- <%= row.getPrice() %></h4></div>
                <div><b>Stock Quantity : <%= row.getStockQuantity() %></b></div>
            </div>
        </div>
        <div class="card-footer">
            <button type="button" class="btn btn-danger btn-sm w-100 delete-product-btn"
                    data-product-id="<%= row.getProductId() %>"
                    data-product-name="<%= row.getProductName() == null ? "" : row.getProductName().replace("\"", "&quot;") %>">
                Delete Product
            </button>
        </div>
    </div>
<%
  }
%>

</div>

<div id="deleteModal" class="delete-modal-backdrop" role="dialog" aria-modal="true">
    <div class="delete-modal">
        <h5>Why delete this product?</h5>
        <p class="product-name" id="deleteProductName"></p>
        <p class="hint">Select a reason before confirming:</p>
        <form id="deleteReasonForm">
            <label class="delete-option">
                <input type="radio" name="reason" value="Poor quality or misleading product" required>
                Poor quality or misleading product
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Counterfeit or prohibited item" required>
                Counterfeit or prohibited item
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Inappropriate or offensive content" required>
                Inappropriate or offensive content
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Incorrect product information" required>
                Incorrect product information
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Customer safety concern" required>
                Customer safety concern
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Duplicate or spam listing" required>
                Duplicate or spam listing
            </label>
            <label class="delete-option">
                <input type="radio" name="reason" value="Other policy violation" required>
                Other policy violation
            </label>
            <div class="delete-actions">
                <button type="button" class="btn btn-secondary btn-sm" onclick="closeDeleteModal()">Keep Product</button>
                <button type="submit" id="confirmDeleteBtn" class="btn btn-danger btn-sm">Confirm Delete</button>
            </div>
        </form>
    </div>
</div>

<script>
    var deleteCtx = "<%= ctx %>";
    var pendingDeleteId = null;

    function showBanner(id, message) {
        var box = document.getElementById(id);
        if (!box) {
            return;
        }
        if (message) {
            box.textContent = message;
        }
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

    function openDeleteModal(productId, productName) {
        pendingDeleteId = productId;
        document.getElementById("deleteProductName").textContent = productName || "this product";
        document.querySelectorAll("#deleteReasonForm input[name='reason']").forEach(function (r) {
            r.checked = false;
        });
        document.getElementById("deleteModal").classList.add("show");
    }
    function closeDeleteModal() {
        pendingDeleteId = null;
        document.getElementById("deleteModal").classList.remove("show");
    }
    document.querySelectorAll(".delete-product-btn").forEach(function (btn) {
        btn.addEventListener("click", function () {
            openDeleteModal(btn.getAttribute("data-product-id"), btn.getAttribute("data-product-name"));
        });
    });
    document.getElementById("deleteReasonForm").addEventListener("submit", function (event) {
        event.preventDefault();
        if (!pendingDeleteId) {
            return;
        }
        var selected = document.querySelector("#deleteReasonForm input[name='reason']:checked");
        if (!selected) {
            showBanner("topError", "Please select a reason.");
            return;
        }
        var productId = pendingDeleteId;
        var reason = selected.value;
        var btn = document.getElementById("confirmDeleteBtn");
        btn.disabled = true;
        fetch(deleteCtx + "/view/product/admin/remove/" + encodeURIComponent(productId)
                + "?reason=" + encodeURIComponent(reason), {
            method: "POST",
            credentials: "same-origin",
            headers: { "Accept": "application/json" }
        }).then(function (response) {
            return response.json().then(function (data) {
                return { ok: response.ok, data: data || {} };
            }).catch(function () {
                return { ok: false, data: { message: "Could not delete product. Please try again." } };
            });
        }).then(function (result) {
            btn.disabled = false;
            closeDeleteModal();
            if (result.ok && result.data.success) {
                var card = document.querySelector('[data-product-card="' + productId + '"]');
                if (card) {
                    card.remove();
                }
                showBanner("topSuccess", result.data.message || "Product deleted successfully!");
                return;
            }
            showBanner("topError", (result.data && result.data.message) || "Could not delete product. Please try again.");
        }).catch(function (err) {
            btn.disabled = false;
            closeDeleteModal();
            showBanner("topError", err.message || "Could not delete product. Please try again.");
        });
    });
    document.getElementById("deleteModal").addEventListener("click", function (e) {
        if (e.target === this) {
            closeDeleteModal();
        }
    });

    (function enableDescSwipe() {
        document.querySelectorAll(".card-desc").forEach(function (el) {
            var isDown = false;
            var startY = 0;
            var scrollTop = 0;
            el.addEventListener("mousedown", function (e) {
                isDown = true;
                startY = e.pageY;
                scrollTop = el.scrollTop;
            });
            el.addEventListener("mouseleave", function () { isDown = false; });
            el.addEventListener("mouseup", function () { isDown = false; });
            el.addEventListener("mousemove", function (e) {
                if (!isDown) return;
                e.preventDefault();
                el.scrollTop = scrollTop - (e.pageY - startY);
            });
        });
    })();
</script>
</body>
</html>
