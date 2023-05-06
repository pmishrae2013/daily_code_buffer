package com.dailycodebuffer.Oauthserver.service;

import com.dailycodebuffer.Oauthserver.entity.User;
import com.dailycodebuffer.Oauthserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*This is the most important part of springSecurity to load the user based on our database.*/
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null){
			throw new UsernameNotFoundException("No User Found");
		}
		/*If the user is present we create a new user Object of UserDetails */
		return new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(),
				user.getEnabled(),
				true,
				true,
				true,
				getAuthorizedMethods(List.of(user.getRole())));
	}

	private List<GrantedAuthority> getAuthorizedMethods( List<String> roles ) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(String role: roles){
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
