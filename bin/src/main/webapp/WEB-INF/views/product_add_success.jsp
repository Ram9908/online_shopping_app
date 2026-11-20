<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
<title>Product Added</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
	body {
		font-family: Arial, sans-serif;
		margin: 0;
		background: #f8f9fa;
	}
	.box {
		text-align: center;
		background: #fff;
		padding: 2rem 2.5rem;
		border-radius: 8px;
		box-shadow: 0 2px 8px rgba(0,0,0,0.08);
		max-width: 480px;
		margin: 4rem auto;
	}
	.message {
		font-size: 1.25rem;
		color: #198754;
		margin-bottom: 1.5rem;
	}
</style>
</head>
<body>
<jsp:include page="vendor_nav.jsp"/>
<%
	String ctx = request.getContextPath();
%>
	<div class="box">
		<p class="message">${successMessage}</p>
		<a href="<%= ctx %>/product_details.jsp" class="btn btn-success me-2">Add Another Product</a>
		<a href="<%= ctx %>/vendor" class="btn btn-secondary">Go to Home</a>
	</div>
<script>
	// Keep browser Forward/Back off the old POST /product entry
	if (window.history && history.replaceState) {
		history.replaceState(null, "", window.location.href);
	}
</script>
</body>
</html>
