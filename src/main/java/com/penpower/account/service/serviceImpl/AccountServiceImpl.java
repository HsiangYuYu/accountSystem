package com.penpower.account.service.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.penpower.account.dao.AccountDao;
import com.penpower.account.exceptionhandler.exception.*;
import com.penpower.account.pojo.PasswordResetInfo;
import com.penpower.account.entity.Account;
import com.penpower.account.service.AccountService;
import com.penpower.account.service.AuthenticationService;
import com.penpower.account.vo.AccountVO;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AuthenticationService authenticationServiceImpl;

	private static final String NOTFOUND_MESSAGE = "Accounts not found";
	private static final String DEFAULT_PASSWORD = "000000";
	private static final String DEFAULT_SALT = BCrypt.gensalt();

	public List<AccountVO> findAll() throws Exception {
		List<Account> accounts = accountDao.findAllByOrderByIdAsc();
		List<AccountVO> accountsVO = new ArrayList<AccountVO>();
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		if (!accounts.isEmpty()) {
			accountsVO = accountsToAccountsVO(accounts);
			return accountsVO;
		} else {
			throw new NotFoundException(String.format(NOTFOUND_MESSAGE));
		}
	}

	public AccountVO findByGuid(UUID guid) {
		Account account = accountDao.findByGuid(guid);
		AccountVO accountVO = accountToAccountVO(account);
		return accountVO;
	}

	private AccountVO accountToAccountVO(Account account) {
		String bossName = getAccountNameByGuid(account.getBossGuid());
		return new AccountVO(account.getGuid(), account.getAccountName(), account.getDisplayName(), account.getRole(),
				bossName, account.getFilePath(), account.getTimestamp());
	}

	private List<AccountVO> accountsToAccountsVO(List<Account> accounts) {
		List<AccountVO> accountsVO = new ArrayList<AccountVO>();
		accounts.stream().forEach(account -> {
			accountsVO.add(accountToAccountVO(account));
		});
		return accountsVO;
	}

	public String getAccountNameByGuid(UUID guid) {
		Account account = accountDao.findByGuid(guid);
		if (null != account) {
			return account.getAccountName();
		} else {
			return null;
		}
	}

	public UUID create(AccountVO accountInfo) throws Exception {
		if (StringUtils.isBlank(accountInfo.getAccountName())) {
			throw new EmptyAccountNameException();
		} else if (!isBossExists(accountInfo.getBossName())) {
			throw new InvalidBossException(accountInfo.getBossName());
		} else {
			Account account = new Account(accountInfo.getAccountName(), accountInfo.getDisplayName(),
					accountInfo.getRole(), accountInfo.getFilePath(), getCurrentTime(),
					BCrypt.hashpw(DEFAULT_PASSWORD, DEFAULT_SALT), DEFAULT_SALT);

			UUID guid = UUID.randomUUID();
			account.setGuid(guid);
			account.setBossGuid(getGuidByAccountName(accountInfo.getBossName()));
			accountDao.save(account);
			return guid;
		}
	}

	private boolean isBossExists(String bossName) {
		if (null == bossName) {
			return true;
		} else {
			return accountDao.existsByAccountName(bossName);
		}
	}

	private boolean isBossValid(String bossName, UUID guid) throws Exception {
		if (!isBossExists(bossName)) {
			return false;
		} else {
			Account account = accountDao.findByAccountName(bossName);
			return !getInValidBosses(guid).contains(account);
		}
	}

	private UUID getGuidByAccountName(String accountName) {
		Account account = accountDao.findByAccountName(accountName);
		if (null != account) {
			return account.getGuid();
		} else {
			return null;
		}
	}

	private Date getCurrentTime() {
		DateTime jodaTime = new DateTime();
		LocalDateTime jodaTimeZone = jodaTime.withZone(DateTimeZone.UTC).toLocalDateTime();
		return jodaTimeZone.toDate();
	}

	public UUID update(AccountVO accountInfo, UUID guid) throws Exception {
		if (!accountDao.existsByGuid(guid)) {
			throw new NotFoundException();
		} else if (!isBossValid(accountInfo.getBossName(), guid)) {
			throw new InvalidBossException(accountInfo.getBossName());
		} else {
			Account account = accountDao.findByGuid(guid);
			UUID bossGuid = getGuidByAccountName(accountInfo.getBossName());

			account.setDisplayName(accountInfo.getDisplayName());
			account.setRole(accountInfo.getRole());
			account.setBossGuid(bossGuid);
			accountDao.save(account);
			return guid;
		}
	}

	public UUID delete(UUID guid) throws Exception {
		if (!accountDao.existsByGuid(guid)) {
			throw new NotFoundException();
		} else {
			accountDao.deleteByGuid(guid);
			return guid;
		}
	}

	public List<Account> getInValidBosses(UUID guid) throws Exception {
		List<Account> invalidBosses = new ArrayList<Account>();
		List<Account> currentInvalidBoss = new ArrayList<Account>();
		List<Account> invalidBossTemp = new ArrayList<Account>();

		currentInvalidBoss = getAccountByBossGuid(guid);
		invalidBosses.addAll(currentInvalidBoss);

		while (!currentInvalidBoss.isEmpty()) {
			currentInvalidBoss.forEach(account -> invalidBossTemp.addAll(getAccountByBossGuid(account.getGuid())));
			invalidBosses.addAll(invalidBossTemp);
			currentInvalidBoss = invalidBossTemp;
			invalidBossTemp.clear();
		}
		invalidBosses.add(accountDao.findByGuid(guid));
		return invalidBosses;
	}

	private List<Account> getAccountByBossGuid(UUID guid) {
		return accountDao.findAccountByBossGuid(guid);
	}

	public List<AccountVO> getValidBosses(UUID guid) throws Exception {
		List<Account> accounts = accountDao.findAll();
		List<Account> invalidBosses = getInValidBosses(guid);
		List<AccountVO> invalidBossesVO = new ArrayList<AccountVO>();
		accounts.removeAll(invalidBosses);

		invalidBossesVO = accountsToAccountsVO(accounts);
		return invalidBossesVO;
	}

	public Account getLoginAccount() throws Exception {
		
		return null;
	/*	String loginAccountName = authenticationServiceImpl.getCurrentLoginAccountName();
		return accountDao.findByAccountName(loginAccountName);*/
	}

	public void resetPassword(PasswordResetInfo passwordResetInfo, UUID guid) throws Exception {
		Account account = accountDao.findByGuid(guid);
		String oldHashedPassword = account.getHashedPassword();
		String inputOldPassword = passwordResetInfo.getOldPassword();
		String inputOldHashedPassword = getHashPassword(account.getAccountName(), inputOldPassword);
		
		String inputNewPassword = passwordResetInfo.getNewPassword();
		if (oldHashedPassword.equals(inputOldHashedPassword)) {
			String newSalt = BCrypt.gensalt();
			String inputNewHashedPassword = BCrypt.hashpw(inputNewPassword, newSalt);
			account.setHashedPassword(inputNewHashedPassword);
			account.setSalt(newSalt);

		} else {
			throw new NotFoundException();
		}
	}

	public String getHashPassword(String accountName, String password) {
		Account account = accountDao.findByAccountName(accountName);
		String salt = account.getSalt();
		return BCrypt.hashpw(password, salt);
	}
}
