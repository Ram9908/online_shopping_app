<%
	String ctx = request.getContextPath();
%>
<div class="d-flex bg-info p-1 justify-content-between"></div>
<div class="d-flex bg-secondary p-2 justify-content-between align-items-center text-light">
    <div class="m-2 text-light">
        <a href="<%= ctx %>/vendor" class="btn btn-light btn-sm m-2">Home</a>
    </div>
    <form class="m-0 d-flex align-items-center" action="<%= ctx %>/p/search" method="get" autocomplete="off">
        <input id="vendorNavSearch" class="form-control form-control-sm" type="search" name="productName"
               placeholder="Search here" style="min-width: 220px;" autocomplete="off"/>
    </form>
    <script>
        (function () {
            var input = document.getElementById("vendorNavSearch");
            if (!input) {
                return;
            }
            function syncSearchBox() {
                var params = new URLSearchParams(window.location.search);
                var q = params.get("productName");
                var onSearchPage = window.location.pathname.indexOf("/p/search") !== -1;
                input.value = (onSearchPage && q) ? q : "";
            }
            syncSearchBox();
            window.addEventListener("pageshow", syncSearchBox);
        })();
    </script>
    <div class="d-flex align-items-center">
        <a href="<%= ctx %>/view/all/products" class="btn btn-light btn-sm m-2">Product View</a>
        <a href="<%= ctx %>/product_details.jsp" class="btn btn-light btn-sm m-2">Add Product</a>
        <a href="<%= ctx %>/logout" class="btn btn-light btn-sm m-2">Sign Out</a>
    </div>
</div>
<jsp:include page="message_banner.jsp"/>
