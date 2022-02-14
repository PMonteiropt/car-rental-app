package com.car.rental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.rental.entity.User;
import com.car.rental.service.IUserService;

@RestController
public class UserController {

	@Autowired
	private IUserService userService;

	@PostMapping({ "/createNewUser" })
	public User registerNewUser(@RequestBody User user) {
		
		
		return userService.createNewUser(user);

	}
	
	@GetMapping("/getUser")
	public User get() {
		
		
		
		return userService.getUserByUserName("kls");
	}
}
