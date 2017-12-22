package com.ef.service;

import java.time.LocalDateTime;

public interface ParseService {
	void processLogs(String filename, LocalDateTime startDate, String duration, Integer threshold);
}
