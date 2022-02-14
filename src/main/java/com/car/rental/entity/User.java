package com.car.rental.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="user")
public class User extends AbstractEntity  {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_key")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userKey;

	private String login;

	private String firstName;

	private String lastName;

	private String userPassword;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_KEY") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_KEY") })
	private Set<Role> userRoles;

	


	public Long getUserKey() {
		return userKey;
	}

	public void setUserKey(Long userKey) {
		this.userKey = userKey;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Set<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}
	
	
	
	
}
