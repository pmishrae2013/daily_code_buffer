package com.dailycodebuffer.Oauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/*If any of the api is required to be configured with Oauth2, then this is the page that needs to be configured.*/
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


}
