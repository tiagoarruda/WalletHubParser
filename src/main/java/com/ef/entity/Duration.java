package com.ef.entity;

import java.time.LocalDateTime;

public enum Duration {
	HOURLY, DAILY;

	public LocalDateTime getEndingDate(LocalDateTime initialDate) {
		switch (this) {
		case HOURLY: return initialDate.plusHours(1L);
		case DAILY: return initialDate.plusDays(1L);
		default: return initialDate;
		}
	}
}
