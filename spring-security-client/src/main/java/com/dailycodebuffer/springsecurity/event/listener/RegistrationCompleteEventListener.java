package com.dailycodebuffer.springsecurity.event.listener;

/*Here the published event is being listened*/

import com.dailycodebuffer.springsecurity.event.RegistrationCompleteEvent;
import com.dailycodebuffer.springsecurity.entity.User;
import com.dailycodebuffer.springsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Slf4j //Causes lombok to generate a logger field.
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent ( RegistrationCompleteEvent event ) {
        /*create verification token for the user and attach to link that the user receives on successful registration
        also on Clicking the link, the user gets redirected back to the application*/
        User user = event.getUser();
        String token = UUID.randomUUID().toString(); // this token is saved in db and also sent with the link, which we use to match with the one stored in db
        userService.saveVerificationTokenForUser(token, user);

        /*send mail to the user*/
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token; // endpoint created for verification link

        /*create a method and pass details to actually implement email triggering feature.
        sendVerificationEmail(url);*/

        log.info("Click the link to verify your account:{}", url);
    }
}
