<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Success</title>
<style>
	body {
		font-family: Arial, sans-serif;
		display: flex;
		justify-content: center;
		align-items: center;
		min-height: 100vh;
		margin: 0;
		background: #f8f9fa;
	}
	.message {
		font-size: 1.25rem;
		color: #198754;
		text-align: center;
	}
</style>
</head>
<body>
	<p class="message">${successMessage}</p>
<%
	String redirectUrl = (String) request.getAttribute("redirectUrl");
	if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
		String target = request.getContextPath() + redirectUrl;
%>
<script>
	setTimeout(function () {
		window.location.href = "<%= target %>";
	}, 2000);
</script>
<%
	}
%>
</body>
</html>
