package com.car.rental.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.car.rental.entity.User;
import com.car.rental.repository.UserRepository;

@Service
@Transactional
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;

	@Override
	public User createNewUser(User user) {
	
		
		user.setUserPassword(getEncodedPassword(user.getUserPassword()));
		
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserName(String userName) {
		
		return userRepository.getUserByUserName(userName);
	}

	
	private String getEncodedPassword(String password) {
		return jwtService.getEncondedPassword(password);
	}
}
