package com.genpact.online_shopping_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="admin")
public class Admin {
	@Id
	private Integer id;
	private String email;
	private String phone;
	private String password;
	
	public Admin() {}

	public Admin(Integer id, String email, String phone, String password) {
		super();
		this.id = id;
		this.email = email;
		this.phone = phone;
		this.password = password;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", email=" + email + ", phone=" + phone + ", password=" + password + "]";
	}
				
}
