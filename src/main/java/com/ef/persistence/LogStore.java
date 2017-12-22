package com.ef.persistence;

import java.time.LocalDateTime;
import java.util.List;

import com.ef.entity.Log;

public interface LogStore {
	Log persist(Log log);
	void persist(List<Log> logs);
	void persistLockMessage(String ip, Integer requests, LocalDateTime initialDate, LocalDateTime endingDate, Integer threshold);
}
