package com.dailycodebuffer.Oauthserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // not required to add getters and setters
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String email;
    @Column(length=60) // not more than 60
    private String password;
    private String role;
    private Boolean enabled=false;
}
