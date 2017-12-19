package com.penpower.account.pojo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TokenExpirationDetail {
	private DateTime loginDate;
	private DateTime expirationDate;

	/**
	 * Set expirationDate with given survivalTime
	 */
	public TokenExpirationDetail(Integer survivalTime) {
		loginDate = new DateTime(DateTimeZone.UTC);
		if (survivalTime == null) {
			expirationDate = null;
		} else {
			expirationDate = loginDate.plusSeconds(survivalTime);
		}
	}

	public DateTime getLoginDate() {
		return loginDate;
	}

	public DateTime getExpirationDate() {
		return expirationDate;
	}
}
