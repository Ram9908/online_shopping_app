<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<%@page import ="com.genpact.online_shopping_app.entity.User"%>
<%! int fontSize; %>
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
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-secondary p-2">
    <a href="<%= ctx %>/vendor" class="btn btn-light btn-sm m-2">Home</a>
</div>
<jsp:include page="message_banner.jsp"/>

<h2>All Products</h2>

<div class="product-grid m-auto p-4">
      
<jsp:useBean id="productItems" type="java.util.List" scope="request"/>  

<%
  for(Iterator<Product> it = productItems.iterator(); it.hasNext(); ){
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
            <form action="<%= ctx %>/view/all/remove/product/<%=row.getProductId()%>" method="get" class="mb-2">
                <div class="d-flex justify-content-center align-items-center gap-2">
                    <h6 class="mb-0">Qty:</h6>
                    <input class="form-control" style="max-width: 70px;" type="number" min="1"
                           max="<%= row.getStockQuantity() %>" value="1" name="quantity" required>
                    <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                </div>
            </form>
            <form action="<%= ctx %>/view/all/update/product/<%=row.getProductId()%>" method="get">
                <button type="submit" class="btn btn-primary btn-sm" style="width: 100%">Update Product</button>
            </form>
        </div>
    </div>
<%
  }  // for loop
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
</script>

</body>
</html>



