package com.dailycodebuffer.springsecurity.service;

import com.dailycodebuffer.springsecurity.entity.User;
import com.dailycodebuffer.springsecurity.entity.VerificationToken;
import com.dailycodebuffer.springsecurity.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser( String token, User user );

	String validateVerification( String token );

	VerificationToken generateNewVerificationToken( String oldToken );

	User findUserByEmail( String email );

	void createResetPasswordTokenForUser( User user, String token );

	String validateResetPasswordToken( String token );

	Optional<User> getUserByPasswordResetToken( String token );

	void changePassword( User user, String newPassword );

	boolean isPasswordValid( User user, String oldPassword );
}
