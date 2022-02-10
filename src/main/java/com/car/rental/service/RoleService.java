package com.car.rental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.rental.entity.Role;
import com.car.rental.repository.RoleRepository;

@Service
public class RoleService {
	
	
	@Autowired
	private RoleRepository roleRepo;
	
	public Role createNewRole(Role role) {
		
		return roleRepo.save(role);
	}

}
