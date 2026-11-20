<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
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
            height: 580px;
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
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-secondary p-2 justify-content-between align-items-center">
    <a href="<%= ctx %>/user" class="btn btn-light btn-sm m-2">Home</a>
    <a href="<%= ctx %>/view/all/cart" class="btn btn-light btn-sm m-2">Cart</a>
</div>
<div id="topSuccess" class="alert alert-success text-center" role="alert" style="display:none; margin:0; border-radius:0;">
    Added to cart successfully!
</div>
<div id="topError" class="alert alert-danger text-center" role="alert" style="display:none; margin:0; border-radius:0;"></div>
<jsp:include page="message_banner.jsp"/>

<h2>All Products</h2>

<div class="product-grid m-auto p-4">
      
<jsp:useBean id="productList" type="java.util.List" scope="request"/>  

<%
  for(Iterator<Product> it = productList.iterator(); it.hasNext(); ){
    Product row = (Product)it.next();
%>
    <div class="card p-2">
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
            <form class="add-cart-form" action="<%= ctx %>/view/add/cart/<%=row.getProductId()%>" method="get">
                <div class="d-flex justify-content-center align-items-center gap-2">
                    <h6 class="mb-0">Qty:</h6>
                    <input class="form-control" style="max-width: 70px;" type="number" min="1"
                           max="<%= row.getStockQuantity() %>" value="1" name="quantity" required>
                    <button type="submit" class="btn btn-success btn-sm">Add to cart</button>
                </div>
            </form>
        </div>
    </div>
<%
  }
%>

</div>

<script>
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

    document.querySelectorAll(".add-cart-form").forEach(function (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault();
            var btn = form.querySelector("button[type='submit']");
            if (btn) {
                btn.disabled = true;
            }
            fetch(form.action + "?" + new URLSearchParams(new FormData(form)).toString(), {
                method: "GET",
                credentials: "same-origin",
                redirect: "follow"
            }).then(function (response) {
                if (btn) {
                    btn.disabled = false;
                }
                if (response.ok || response.redirected) {
                    showBanner("topSuccess");
                    return;
                }
                throw new Error("Could not add to cart. Please try again.");
            }).catch(function (err) {
                if (btn) {
                    btn.disabled = false;
                }
                showBanner("topError", err.message || "Could not add to cart. Please try again.");
            });
        });
    });
</script>

</body>
</html>
