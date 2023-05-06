package com.dailycodebuffer.springsecurity.controller;

import com.dailycodebuffer.springsecurity.entity.User;
import com.dailycodebuffer.springsecurity.entity.VerificationToken;
import com.dailycodebuffer.springsecurity.event.RegistrationCompleteEvent;
import com.dailycodebuffer.springsecurity.model.PasswordModel;
import com.dailycodebuffer.springsecurity.model.UserModel;
import com.dailycodebuffer.springsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher; // used for triggering email
    @PostMapping("/hello")
    public String sayHello(){
        return "Hello, this is from spring-security-client";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, HttpServletRequest request){
        User user = userService.registerUser(userModel);
        publisher.publishEvent (new RegistrationCompleteEvent(user, applicationUrl(request))); // here to publish any event, firstly an event must be created
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration( @RequestParam("token") String token ){
        String result = userService.validateVerification(token);
        if (result.equalsIgnoreCase("valid")){
            return "User verified";
        }
        return "Bad User";
    }
    @GetMapping("/resendToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenByMail(user, applicationUrl(request), verificationToken);
        return "Verification Link sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword( @RequestBody PasswordModel passwordModel, HttpServletRequest request ){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user!=null){
            String token = UUID.randomUUID().toString();
            userService.createResetPasswordTokenForUser(user,token);
            url = passwordResetTokenUrl(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/saveNewPassword")
    public String savePassword(@RequestParam("token")String token, @RequestBody PasswordModel passwordModel){
        String result = userService.validateResetPasswordToken(token);
        if (!result.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }
        Optional<User> userOptional = userService.getUserByPasswordResetToken(token);
        if (userOptional.isPresent()){
            userService.changePassword(userOptional.get(), passwordModel.getNewPassword());
            return "Password reset Successfully";
        } else{
            return "Invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.isPasswordValid(user, passwordModel.getOldPassword())){
            return "Invalid Old Password";
        }
        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed Successfully";
    }

    private String passwordResetTokenUrl( User user, String applicationUrl, String token ) {
        String url = applicationUrl + "/saveNewPassword?token=" + token;
        log.info("Click the link to Reset your password:{}", url);
        return url;
    }

    private void resendVerificationTokenByMail( User user, String applicationUrl, VerificationToken verificationToken ) {
        /*send mail to the user*/
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken(); // endpoint created for verification link

        /*create a method and pass details to actually implement email triggering feature.
        sendVerificationEmail(url);*/

        log.info("Click the link to verify your account:{}", url);
    }

    private String applicationUrl( HttpServletRequest request ) {
        return "http://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath();
    }
}