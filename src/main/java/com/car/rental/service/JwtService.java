package com.car.rental.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.car.rental.entity.JwtRequest;
import com.car.rental.entity.JwtResponse;
import com.car.rental.entity.User;
import com.car.rental.repository.UserRepository;
import com.car.rental.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
		String userName = jwtRequest.getUserName();
		String password = jwtRequest.getPassword();

		authenticate(userName, password);

		final UserDetails userDetails = loadUserByUsername(userName);

		String newGeneratedToken = jwtUtil.generateToken(userDetails);

		User user = userRepository.getUserByUserName(userName);

		return new JwtResponse(user, newGeneratedToken);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.getUserByUserName(username);

		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getUserPassword(),
					getAuthorities(user));
		} else {
			throw new UsernameNotFoundException("Username is not valid");
		}

	}

	private Set getAuthorities(User user) {
		Set authorities = new HashSet<>();

		user.getUserRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});

		return authorities;
	}

	private void authenticate(String userName, String password) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		} catch (DisabledException e) {

			throw new Exception("User is disabled");
		} catch (BadCredentialsException e) {

			throw new Exception("Bad credentials");
		}
	}

	public String getEncondedPassword(String password) {
		return passwordEncoder.encode(password);
	}

}
