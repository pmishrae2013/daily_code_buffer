package com.dailycodebuffer.springsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;

    /*One to one mapping of token for each user*/
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name="FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

    private Date calculateExpirationTime( int expirationTime ) {
        Calendar calendar = Calendar.getInstance(); // gets the current instance of time along with date
        calendar.setTimeInMillis(new Date().getTime()); // picks out only the time
        calendar.add(Calendar.MINUTE, expirationTime); // gets the minute part and adds the expiration time in it(here 10 mins)
        return new Date(calendar.getTime().getTime());
    }
}
