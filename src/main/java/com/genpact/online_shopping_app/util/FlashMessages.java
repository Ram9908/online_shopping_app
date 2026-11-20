package com.genpact.online_shopping_app.util;

import jakarta.servlet.http.HttpSession;

/** One-time success/error banners shown on the next page after a redirect. */
public final class FlashMessages {

	private static final String SUCCESS = "flashSuccess";
	private static final String ERROR = "flashError";

	private FlashMessages() {
	}

	public static void success(HttpSession session, String message) {
		if (session != null) {
			session.setAttribute(SUCCESS, message);
		}
	}

	public static void error(HttpSession session, String message) {
		if (session != null) {
			session.setAttribute(ERROR, message);
		}
	}
}
