<%
	String flashSuccess = null;
	String flashError = null;
	if (session != null) {
		flashSuccess = (String) session.getAttribute("flashSuccess");
		flashError = (String) session.getAttribute("flashError");
		if (flashSuccess != null) {
			session.removeAttribute("flashSuccess");
		}
		if (flashError != null) {
			session.removeAttribute("flashError");
		}
	}
%>
<% if (flashSuccess != null && !flashSuccess.trim().isEmpty()) { %>
<div class="alert alert-success js-auto-hide-alert" role="alert"><%= flashSuccess %></div>
<% } %>
<% if (flashError != null && !flashError.trim().isEmpty()) { %>
<div class="alert alert-danger js-auto-hide-alert" role="alert"><%= flashError %></div>
<% } %>
<script>
(function () {
	var alerts = document.querySelectorAll(".js-auto-hide-alert");
	if (!alerts.length) {
		return;
	}
	setTimeout(function () {
		alerts.forEach(function (el) {
			if (!el || el.style.display === "none") {
				return;
			}
			el.style.transition = "opacity 0.4s ease";
			el.style.opacity = "0";
			setTimeout(function () {
				if (el && el.parentNode) {
					el.remove();
				}
			}, 400);
		});
	}, 2000);
})();
</script>
