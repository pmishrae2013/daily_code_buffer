package com.dailycodebuffer.Oauthserver.repository;

import com.dailycodebuffer.Oauthserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// This repository is of type 'User' and 'id' of type 'Long'
public interface UserRepository extends JpaRepository<User, Long > {
	User findByEmail( String email );
}
