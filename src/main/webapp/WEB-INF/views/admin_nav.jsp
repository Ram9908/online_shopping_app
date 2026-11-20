<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-info p-1 justify-content-between"></div>
<div class="d-flex bg-secondary p-2 justify-content-between text-light">
    <div class="m-2 text-light">
        <span class="m-2"><a href="<%= ctx %>/admin" class="text-decoration-none text-light">Home</a></span>
    </div>
    <div class="d-flex">
        <span class="m-2 border-colour-dark"><a href="<%= ctx %>/view/product/admin" class="text-decoration-none text-light mx-3">Products</a></span>
        <span class="m-2 border-colour-dark"><a href="<%= ctx %>/add/vendor" class="text-decoration-none text-light mx-3">Vendors</a></span>
        <span class="m-2 border-colour-dark"><a href="<%= ctx %>/add/user" class="text-decoration-none text-light mx-3">Users</a></span>
        <span class="m-2 border-colour-dark"><a href="<%= ctx %>/orders" class="text-decoration-none text-light mx-3">Orders</a></span>
    </div>
</div>
