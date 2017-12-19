package com.penpower.account.service.serviceImpl;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.penpower.account.service.AuthenticationService;
import com.penpower.account.vo.AccountVO;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final int TOKEN_LENGTH = 40;
	private static ConcurrentHashMap<String, Authentication> tokenToAuthentication = new ConcurrentHashMap<>();
	private static final String TOKEN_PREFIX = "Bearer";

	public Authentication getAuthenticationByToken(String token) {
		return tokenToAuthentication.get(token);
	}

	public void storeToken(String token, Authentication authentication) {
		
		tokenToAuthentication.forEach((key, value) -> {
			if (isEquals(value, authentication)) { // If token exists, replace old token by new token	
				tokenToAuthentication.remove(key);
			}
		});
		tokenToAuthentication.put(token, authentication);
	}

	private boolean isEquals(Authentication value, Authentication authentication) {
		if(value.getPrincipal().equals(authentication.getPrincipal())) {
			return true;
		}else {
			return false;
		}
	}

	public void expireToken() {
		final String currentLoginAccountName = SecurityContextHolder.getContext().getAuthentication().getName();
		tokenToAuthentication.forEach((key, value) -> {
			if (value.getName().equals(currentLoginAccountName)) {
				tokenToAuthentication.remove(key);
			}
		});
	}

	public void expireToken(String token) {
		//currentLoginAccountName = null;
		tokenToAuthentication.remove(token);
	}

	public String generateToken() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z')
				.filteredBy(LETTERS, DIGITS).build();
		String token = generator.generate(TOKEN_LENGTH);
		return TOKEN_PREFIX + " " + token;
	}

}
