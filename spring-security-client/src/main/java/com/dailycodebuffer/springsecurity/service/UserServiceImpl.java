package com.dailycodebuffer.springsecurity.service;

import com.dailycodebuffer.springsecurity.entity.PasswordResetToken;
import com.dailycodebuffer.springsecurity.entity.User;
import com.dailycodebuffer.springsecurity.entity.VerificationToken;
import com.dailycodebuffer.springsecurity.model.UserModel;
import com.dailycodebuffer.springsecurity.repository.PasswordResetTokenRepository;
import com.dailycodebuffer.springsecurity.repository.UserRepository;
import com.dailycodebuffer.springsecurity.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private static final String USER_ROLE = "USER";
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole(USER_ROLE);
        /*password before being saved to db is being encoded here*/
        user.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser( String token, User user ) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerification( String token ) {
        /*fetching the user based on the token received*/
        VerificationToken verificationToken= verificationTokenRepository.findByToken(token);
        if (verificationToken == null){
            return "Invalid token"; //user not found based on the token
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime())<=0){
            verificationTokenRepository.delete(verificationToken);
            return "Token expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken( String oldToken ) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail( String email ) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createResetPasswordTokenForUser( User user, String token ) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validateResetPasswordToken( String token ) {
        PasswordResetToken passwordResetToken= passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null){
            return "Invalid token"; //user not found based on the token
        }
        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime())<=0){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "Token expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken( String token ) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void  changePassword( User user, String newPassword ) {
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isPasswordValid( User user, String oldPassword ) {
        return bCryptPasswordEncoder.matches(oldPassword,user.getPassword());
    }

}
