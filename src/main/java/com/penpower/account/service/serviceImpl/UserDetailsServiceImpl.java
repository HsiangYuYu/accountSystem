package com.penpower.account.service.serviceImpl;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.penpower.account.dao.AccountDao;
import com.penpower.account.entity.Account;
import com.penpower.account.pojo.Role;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountDao accountDao;
	private static final String USER_NOT_FOUND_MESSAGE = "Invalid accountName/password";
	private static final String DEFAULT_PASSWORD = "000000";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountDao.findByAccountName(username);
		
		if (null == account) {
			throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
		}
		
	
		
		Role role = account.getRole();
		UserBuilder userBuilder = User.withDefaultPasswordEncoder();
		UserDetails userDetails = userBuilder
				.username(account.getAccountName())
				.password(account.getHashedPassword())
				.roles(role.toString())
				.build();

		return userDetails;
	}
}
