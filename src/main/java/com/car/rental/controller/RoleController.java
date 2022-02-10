package com.car.rental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.entity.Role;
import com.car.rental.service.RoleService;

@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	
	@GetMapping("/get")
	public String get() {
		
		return "Olá";
	}

	@PostMapping({ "/createNewRole" })
	public Role getLogin(@RequestBody Role role) {
		System.out.println();
		return roleService.createNewRole(role);
	}

}
