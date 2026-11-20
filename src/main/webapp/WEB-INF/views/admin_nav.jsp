<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-info p-1 justify-content-between"></div>
<div class="d-flex bg-secondary p-2 justify-content-between text-light">
    <div class="m-2 text-light">
        <a href="<%= ctx %>/admin" class="btn btn-light btn-sm m-2">Home</a>
    </div>
    <div class="d-flex align-items-center">
        <a href="<%= ctx %>/view/product/admin" class="btn btn-light btn-sm m-2">Products</a>
        <a href="<%= ctx %>/add/vendor" class="btn btn-light btn-sm m-2">Vendors</a>
        <a href="<%= ctx %>/add/user" class="btn btn-light btn-sm m-2">Users</a>
        <a href="<%= ctx %>/orders" class="btn btn-light btn-sm m-2">Orders</a>
        <a href="<%= ctx %>/logout" class="btn btn-light btn-sm m-2">Sign Out</a>
    </div>
</div>
<jsp:include page="message_banner.jsp"/>
