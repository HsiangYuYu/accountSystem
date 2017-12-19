package com.penpower.account.service;


import org.springframework.security.core.Authentication;


public interface AuthenticationService {

	void storeToken(String token, Authentication authentication);

	void expireToken();
	
	String generateToken();

	void expireToken(String token);
}
