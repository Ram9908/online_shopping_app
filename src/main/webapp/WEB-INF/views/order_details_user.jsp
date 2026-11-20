<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="java.util.*"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.genpact.online_shopping_app.entity.OrderEntity"%>
<%@page import ="com.genpact.online_shopping_app.entity.Product"%>
<%@page import ="com.genpact.online_shopping_app.entity.Address"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <style>
        body {
            background-color: #eaf6ff;
            font-family: Arial, sans-serif;
        }
        .order-img {
            max-height: 120px;
            max-width: 100%;
            width: auto;
            object-fit: contain;
        }
        .btn-cancel-order {
            padding: 0.2rem 0.55rem;
            font-size: 0.8rem;
            line-height: 1.2;
            border-radius: 6px;
        }
        .desc-cell {
            max-width: 220px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .cancel-modal-backdrop {
            display: none;
            position: fixed;
            inset: 0;
            background: rgba(0, 0, 0, 0.45);
            z-index: 1050;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }
        .cancel-modal-backdrop.show {
            display: flex;
        }
        .cancel-modal {
            background: #fff;
            border-radius: 10px;
            max-width: 420px;
            width: 100%;
            padding: 1.25rem 1.4rem;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.18);
        }
        .cancel-modal h5 {
            margin-bottom: 0.85rem;
        }
        .cancel-option {
            display: block;
            margin-bottom: 0.55rem;
        }
    </style>
</head>
<body>
<%
	String ctx = request.getContextPath();
	DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("dd-MM-yyyy");
%>
<div class="d-flex bg-secondary p-2 justify-content-between align-items-center">
    <a href="<%= ctx %>/user" class="btn btn-light btn-sm m-2">Home</a>
    <div class="d-flex align-items-center">
        <a href="<%= ctx %>/view/all" class="btn btn-light btn-sm m-2">Products</a>
        <a href="<%= ctx %>/logout" class="btn btn-light btn-sm m-2">Sign Out</a>
    </div>
</div>
<jsp:include page="message_banner.jsp"/>

    <div class="container mt-4">
        <h2 class="text-center mb-4">Order List</h2>
        <div class="table-responsive bg-white rounded shadow-sm p-2">
        <table class="table table-bordered table-striped align-middle mb-0">
            <thead class="table-dark">
                <tr>
                    <th>Product Image</th>
                    <th>Product Name</th>
                    <th>Product Description</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Address Line 1</th>
                    <th>City</th>
                    <th>Zip Code</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                <%  List<OrderEntity> updatedOrder= (List<OrderEntity>) request.getAttribute("updatedOrder");
                    if (updatedOrder != null) {
                        for (OrderEntity order : updatedOrder) {
                                	List<Product> productList = (List<Product>) request.getAttribute("productList");
                                	for(Product product:productList){
                                	  if(product.getProductId().equals(order.getVirtualProductId())){
                                		  List<Address> addressList = (List<Address>) request.getAttribute("addressList");
                                		  for(Address address:addressList){
                                			  if(order.getVirtualAddressId().equals(address.getId())){
                                				  String orderDateText = order.getOrderDate() != null
                                						  ? order.getOrderDate().toLocalDate().format(dateOnly)
                                						  : "";
                %>
                <tr>
                <td><img class="order-img" src="/Images/<%=product.getImageUrl()%>" alt="img"></td>
                <td><%= product.getProductName() %></td>
                <td class="desc-cell" title="<%= product.getProductDescription() %>"><%= product.getProductDescription() %></td>
                <td><%= orderDateText %></td>
                <td><%= order.getTotalAmount() %></td>
                <td><%= address.getAddressLine1() %></td>
                <td><%= address.getCity() %></td>
                <td><%= address.getZipCode() %></td>
                <td>
                    <button type="button" class="btn btn-outline-danger btn-cancel-order"
                            onclick="openCancelModal('<%= order.getOrderId() %>')">Cancel</button>
                   </td>
            </tr>
                <%
                                	  }
                                	}
                                }
                          }
                    }
                    }
                %>
            </tbody>
        </table>
        </div>
    </div>

<div id="cancelModal" class="cancel-modal-backdrop" role="dialog" aria-modal="true">
    <div class="cancel-modal">
        <h5>Why do you want to cancel?</h5>
        <form id="cancelOrderForm" method="get">
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Quality issue" required>
                Quality issue
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Got same product for better price" required>
                Got same product for better price
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="I changed my mind" required>
                I changed my mind
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Want to buy other product" required>
                Want to buy other product
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Ordered by mistake" required>
                Ordered by mistake
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Delivery taking too long" required>
                Delivery taking too long
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Wrong size or color selected" required>
                Wrong size or color selected
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Product no longer needed" required>
                Product no longer needed
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Found a better alternative" required>
                Found a better alternative
            </label>
            <label class="cancel-option">
                <input type="radio" name="cancelReason" value="Payment or billing issue" required>
                Payment or billing issue
            </label>
            <div class="d-flex justify-content-end gap-2 mt-3">
                <button type="button" class="btn btn-secondary btn-sm" onclick="closeCancelModal()">Back</button>
                <button type="submit" class="btn btn-danger btn-sm">Confirm Cancel</button>
            </div>
        </form>
    </div>
</div>

<script>
    var cancelCtx = "<%= ctx %>";
    function openCancelModal(orderId) {
        var form = document.getElementById("cancelOrderForm");
        form.action = cancelCtx + "/delete/" + orderId;
        form.querySelectorAll("input[name='cancelReason']").forEach(function (r) {
            r.checked = false;
        });
        document.getElementById("cancelModal").classList.add("show");
    }
    function closeCancelModal() {
        document.getElementById("cancelModal").classList.remove("show");
    }
    document.getElementById("cancelModal").addEventListener("click", function (e) {
        if (e.target === this) {
            closeCancelModal();
        }
    });
</script>
</body>
</html>
