package com.car.rental.service;

import com.car.rental.entity.User;

public interface IUserService {

	
	
		public User createNewUser(User user);
		
		public User getUserByUserName(String userName);
}
