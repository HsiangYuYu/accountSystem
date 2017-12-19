package com.penpower.account.vo;


import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import com.penpower.account.pojo.Role;

public class AccountVO {
	private UUID guid;
	private String accountName;
	private String displayName;
	private Role role;
	private String bossName;
	private String filePath;
	private Date timestamp;

	public AccountVO(UUID guid, String accountName, String displayName, Role role, String bossName, String filePath,
			Date timestamp) {
		this.guid = guid;
		this.accountName = accountName;
		this.displayName = displayName;
		this.role = role;
		this.bossName = bossName;
		this.filePath = filePath;
		this.timestamp = timestamp;
	}

	
	public AccountVO() {
	
	}


	public UUID getGuid() {
		return guid;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Role getRole() {
		return role;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public String getFilePath() {
		return filePath;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
}
