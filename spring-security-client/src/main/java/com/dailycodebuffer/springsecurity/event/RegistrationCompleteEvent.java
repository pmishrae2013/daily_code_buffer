package com.dailycodebuffer.springsecurity.event;

/*Here an Event is being created that is to be published, and will be listened after that.
 * where there is an event there is a Listener*/

import com.dailycodebuffer.springsecurity.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    /*this applicationUrl is the url that the registered user receives to log-in on successfully registering oneself*/
    private String applicationUrl;
    public RegistrationCompleteEvent ( User user, String applicationUrl ) {
        super (user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}