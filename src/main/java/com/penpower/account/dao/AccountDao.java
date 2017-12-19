package com.penpower.account.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.penpower.account.entity.Account;
import com.penpower.account.pojo.Role;

@Repository
public interface AccountDao extends JpaRepository<Account, UUID> {
	/*Account findById(int id);

	Account findByAccountName(String accountName);

	Account saveAndFlush(Account account);
	
	List<Account> findAccountByBossId(int bossId);

	void deleteById(Integer id);
	
	List<Account> findAll();
	
	List<Account> findAllByOrderByIdAsc();*/
	Account findByGuid(UUID guid);
	
	boolean existsByGuid(UUID guid);
	
	boolean existsByAccountName(String accountName);

	Account findByAccountName(String accountName);

	Account saveAndFlush(Account account);
	
	List<Account> findAccountByBossGuid(UUID bossGuid);

	void deleteByGuid(UUID guid);
	
	//List<Account> findAll();
	
	List<Account> findAllByOrderByIdAsc();
	
}
