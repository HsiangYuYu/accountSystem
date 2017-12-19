package com.penpower.account.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.penpower.account.pojo.TokenExpirationDetail;
import com.penpower.account.service.serviceImpl.AuthenticationServiceImpl;

public class AuthenticationFilter extends GenericFilterBean {

	AuthenticationServiceImpl authenticationServiceImpl = new AuthenticationServiceImpl();

	private static final String AUTHORIZATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		/* Get authentication by Authorization token header,
		 * if authentication is invalid, the authentication will be set to NULL. 
		 */
		Authentication authentication = getAuthentication((HttpServletRequest) request);

		/*
		 *  Set authentication to SecurityContextHolder,
		 *  if authentication is null, the authentication can't pass this filter.
		 */
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);

	}

	/**
	 * Get authentication with request header,
	 * if authentication is invalid return null,
	 * else return authentication.
	 * */
	private Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if (token != null) {
			Authentication authentication = authenticationServiceImpl.getAuthenticationByToken(token);
			return checkAuthenticationValidity(authentication, token);
		} else {
			return null;
		}
	}

	/**
	 * Check authentication is valid or not 
	 */
	private Authentication checkAuthenticationValidity(Authentication authentication, String token) {
		if (authentication != null) {
			return checkAuthenticationExpiration(authentication, token);
		} else {
			return null;
		}
	}

	/**
	 * Check authentication is expired or not
	 */
	private Authentication checkAuthenticationExpiration(Authentication authentication, String token) {
		DateTime expirationDate = ((TokenExpirationDetail) authentication.getDetails()).getExpirationDate();
		if (null == expirationDate || expirationDate.isAfterNow()) {
			return authentication;
		} else {
			authenticationServiceImpl.expireToken(token);
			return null;
		}
	}
}
