package com.genpact.online_shopping_app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="vendor")
public class Vendor {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vendorId;
	@Column(name="vendorName")
	private String vendorName;
	private String email;
	private String password;
	@Column(name="phone")
	private String phone;
	

	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
	
	
	public Vendor() {}
			
	
	public Vendor(String vendorName, String email, String password, String phone, Role role) {
		super();
		this.vendorName = vendorName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
	}


	public Integer getVendorId() {
		return vendorId;
	}


	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}


	public String getVendorName() {
		return vendorName;
	}


	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	@Override
	public String toString() {
		return "Vendor [vendorId=" + vendorId + ", vendorName=" + vendorName + ", email=" + email + ", password="
				+ password + ", phone=" + phone + ", role=" + role + "]";
	}


	
	

}
