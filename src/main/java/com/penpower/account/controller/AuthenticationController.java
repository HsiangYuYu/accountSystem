package com.penpower.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.penpower.account.service.serviceImpl.AuthenticationServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	private static final String LOGIN_SUCCESS = "Login Successfully";
	private static final String LOGOUT_SUCCESS = "Logout Successfully";

	@Autowired
	AuthenticationServiceImpl authenticationServiceImpl;

	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String login() throws Exception {
		return LOGIN_SUCCESS;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout() throws Exception {
		authenticationServiceImpl.expireToken();
		return LOGOUT_SUCCESS;
	}
	
}
