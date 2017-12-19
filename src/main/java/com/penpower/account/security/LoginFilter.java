package com.penpower.account.security;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penpower.account.pojo.LoginInfo;
import com.penpower.account.pojo.TokenExpirationDetail;
import com.penpower.account.service.AccountService;
import com.penpower.account.service.AuthenticationService;
import com.penpower.account.service.serviceImpl.AccountServiceImpl;
import com.penpower.account.service.serviceImpl.AuthenticationServiceImpl;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

	AuthenticationService authenticationServiceImpl = new AuthenticationServiceImpl();
	// AccountService accountServiceImpl = new AccountServiceImpl();
	private AccountService accountServiceImpl = null;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String INPUT_ACCOUNT_NAME = "accountName";
	private static final String INPUT_PASSWORD = "password";

	// TOKEN_SURVIVAL_SECONDS = null
	// means token survival infinitely until server
	// shutdown or account logout.
	private static final Integer TOKEN_SURVIVAL_SECONDS = 10 * 24 * 60 * 60;

	public LoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		ServletContext servletContext = request.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		accountServiceImpl = webApplicationContext.getBean(AccountService.class);
		super.doFilter(request, response, chain);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LoginInfo loginInfo = getRequestInfo(request);

		String accountName = loginInfo.getAccountName();
		String password = loginInfo.getPassword();
		String hashPassword = accountServiceImpl.getHashPassword(accountName, password);
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(accountName,
				hashPassword);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {

		TokenExpirationDetail tokenExpirationDetail = new TokenExpirationDetail(TOKEN_SURVIVAL_SECONDS);

		// Add token expiration details to authentication
		((UsernamePasswordAuthenticationToken) authentication).setDetails(tokenExpirationDetail);
		addAuthentication(response, authentication);

		System.out.println(authentication);

		chain.doFilter(request, response);
	}

	/**
	 * Get request information with different request dataType
	 * 
	 * @return request information.
	 */
	private LoginInfo getRequestInfo(ServletRequest request) throws IOException {

		LoginInfo authInfo = new LoginInfo();

		if (request.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			authInfo.setAccountName(request.getParameter(INPUT_ACCOUNT_NAME));
			authInfo.setPassword(request.getParameter(INPUT_PASSWORD));
			return authInfo;
		} else {
			try (BufferedReader bufferedReader = new BufferedReader(request.getReader())) {
				ObjectMapper mapper = new ObjectMapper();
				authInfo = mapper.readValue(bufferedReader, LoginInfo.class);
				return authInfo;
			}
		}
	}

	/**
	 * Add authentication: Store token and set response header
	 */
	private void addAuthentication(HttpServletResponse response, Authentication authentication) {

		String token = authenticationServiceImpl.generateToken();
		authenticationServiceImpl.storeToken(token, authentication);

		response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION_HEADER);
		response.setHeader(AUTHORIZATION_HEADER, token);
		response.setDateHeader("Expires",
				((TokenExpirationDetail) authentication.getDetails()).getExpirationDate().getMillis());

	}
}
