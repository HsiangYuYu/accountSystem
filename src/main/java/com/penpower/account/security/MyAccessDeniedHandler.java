package com.penpower.account.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	private static final String ACCESS_DENIED_MESSAGE = "You don't have permission";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		response.sendError(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED_MESSAGE);

	}

}
