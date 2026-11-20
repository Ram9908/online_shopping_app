package com.genpact.online_shopping_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blocked_email")
public class BlockedEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "email", nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "account_type", length = 32)
	private String accountType;

	@Column(name = "reason", length = 512)
	private String reason;

	@Column(name = "blocked_at")
	private LocalDateTime blockedAt;

	public BlockedEmail() {
	}

	public BlockedEmail(String email, String accountType, String reason) {
		this.email = email;
		this.accountType = accountType;
		this.reason = reason;
		this.blockedAt = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getBlockedAt() {
		return blockedAt;
	}

	public void setBlockedAt(LocalDateTime blockedAt) {
		this.blockedAt = blockedAt;
	}
}
