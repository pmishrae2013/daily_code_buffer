package com.dailycodebuffer.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
//    try validating the pattern of the password using regex pattern
    private String password;
    private String matchingPassword;
}
