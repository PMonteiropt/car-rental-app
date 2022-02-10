package com.car.rental.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role extends AbstractEntity {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="role_key")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long roleKey;
	
	
	private String roleName;


	public Long getKey() {
		return roleKey;
	}


	public void setRoleKey(Long roleKey) {
		this.roleKey = roleKey;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	

}
