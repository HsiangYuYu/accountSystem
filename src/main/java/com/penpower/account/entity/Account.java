package com.penpower.account.entity;

import java.util.Date;
import java.util.UUID;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.penpower.account.pojo.Role;

@Entity
public class Account {

	@Id
	@GeneratedValue
	private int id;

	private UUID guid;

	private String accountName;
	private String displayName;

	@Enumerated(EnumType.STRING)
	private Role role;

	private UUID bossGuid;

	private String filePath;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	private String hashedPassword;
	
	private String salt;

	public Account(String accountName, String displayName, Role role, String filePath, Date timestamp, String hashedPassword, String salt) {
		this.accountName = accountName;
		this.displayName = displayName;
		this.role = role;
		this.filePath = filePath;
		this.timestamp = timestamp;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}

	public Account() {

	}

	public int getId() {
		return id;
	}

	public UUID getGuid() {
		return guid;
	}

	public void setGuid(UUID guid) {
		this.guid = guid;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UUID getBossGuid() {
		return bossGuid;
	}

	public void setBossGuid(UUID bossGuid) {
		this.bossGuid = bossGuid;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimstamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((bossGuid == null) ? 0 : bossGuid.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		result = prime * result + id;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (bossGuid == null) {
			if (other.bossGuid != null)
				return false;
		} else if (!bossGuid.equals(other.bossGuid))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		if (id != other.id)
			return false;
		if (role != other.role)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
	

}
