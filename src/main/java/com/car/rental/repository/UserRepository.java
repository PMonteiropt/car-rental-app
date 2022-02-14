package com.car.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.car.rental.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

		@Query("SELECT us from user us where us.login=?1")
		public User getUserByUserName(String login);
	
}
