package com.penpower.account.exceptionhandler;



import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.penpower.account.exceptionhandler.exception.*;

@ControllerAdvice
public class ExceptionHandlerHelper extends ResponseEntityExceptionHandler {

	private static final String STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";

	@ExceptionHandler(NotFoundException.class)
	public void  handleNotFoundException(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	@ExceptionHandler({ SelfDeleteException.class, EmptyAccountNameException.class, InvalidBossException.class})
	public void handleBadRequestException(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_CONFLICT);
	}
	
	@ExceptionHandler(IOException.class)
	public void handleIOException(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File IO error");
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public void handleIllegalArgumentException(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid salt for BCrypt");
	}
	
	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
		Integer status = getStatus(request);
		response.sendError(status, exception.getMessage());
	}

	private Integer getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute(STATUS_CODE_ATTRIBUTE);
		if (statusCode == null) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return statusCode;
	}
}
