package com.penpower.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.penpower.account.entity.Account;
import com.penpower.account.pojo.PasswordResetInfo;
import com.penpower.account.service.AccountService;
import com.penpower.account.vo.AccountVO;

@CrossOrigin
@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired 
	private AccountService accountServiceImpl;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<AccountVO> findAll() throws Exception {
		return accountServiceImpl.findAll();
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.GET)
	public AccountVO findByGuid(@PathVariable("guid") UUID guid) throws Exception {
		return accountServiceImpl.findByGuid(guid);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UUID createAccount(@RequestBody AccountVO accountInfo) throws Exception {
		return accountServiceImpl.create(accountInfo);
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UUID updateAccount(@RequestBody AccountVO accountInfo, @PathVariable("guid") UUID guid) throws Exception {
		return accountServiceImpl.update(accountInfo, guid);
	}

	@RequestMapping(value = "/{guid}", method = RequestMethod.DELETE)
	public UUID deleteAccount(@PathVariable("guid") UUID guid) throws Exception {
		return accountServiceImpl.delete(guid);
	}

	@RequestMapping(value = "/{guid}/getValidBoss", method = RequestMethod.GET)
	public List<AccountVO> getValidBoss(@PathVariable("guid") UUID guid) throws Exception {
		return accountServiceImpl.getValidBosses(guid);
	}
	
	@RequestMapping(value = "/{guid}/resetPassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String resetPassword(@RequestBody PasswordResetInfo passwordResetInfo, @PathVariable("guid") UUID guid) throws Exception{
		accountServiceImpl.resetPassword(passwordResetInfo, guid);
		return "success";
	}
	@RequestMapping("/getLoginAccount")
	public Account getLoginAccount() throws Exception {
		return null;
		//return accountServiceImpl.getLoginAccount();
	}

}
