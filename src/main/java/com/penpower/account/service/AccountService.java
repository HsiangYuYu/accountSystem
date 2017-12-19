package com.penpower.account.service;

import java.util.List;
import java.util.UUID;

import com.penpower.account.entity.Account;
import com.penpower.account.pojo.PasswordResetInfo;
import com.penpower.account.vo.AccountVO;
public interface AccountService {
	public List<AccountVO> findAll() throws Exception;
	
	public AccountVO findByGuid(UUID guid) throws Exception;
	
	public UUID create(AccountVO accountInfo) throws Exception;

	public UUID update(AccountVO accountInfo, UUID guid) throws Exception;
	
	public UUID delete(UUID guid) throws Exception;
	
	public List<AccountVO> getValidBosses(UUID guid) throws Exception;
	
	public List<Account> getInValidBosses(UUID guid) throws Exception;
	
	
	public void resetPassword(PasswordResetInfo passwordResetInfo, UUID guid) throws Exception;
//	public Account findById(int id) throws Exception;
//
//	public int create(Account account) throws Exception;
//
//	public int update(Account account, int id) throws Exception;
//
//	public int delete(int id) throws Exception;
//		
//	//public List<Account> getValidBoss(int id) throws Exception;
//	
//	public Account getLoginAccount() throws Exception;

	public String getHashPassword(String accountName, String password);

	
}
