package com.ef.service;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ef.entity.Log;
import com.ef.exception.LogNotFoundException;
import com.ef.exception.ParseLogException;
import com.ef.persistence.LogStore;


public class ParseServiceTest {
	ParseServiceImpl service;
	LogStore store;
	
	@Before
	public void setUp() throws Exception {
		service = new ParseServiceImpl();
		store = mock(LogStore.class);
		service.setStore(store);
	}
	
	@Test
	public void testParseFile() {
		service.parseFile("src/test/resources/input/access.log");
		assert(service.getLogs() != null);
		assert(!service.getLogs().isEmpty());
		assert(service.getLogs().size() == 116484);
	}

	@Test(expected = LogNotFoundException.class)
	public void testFileNotFound() {
		service.parseFile("");
	}
	
	@Test(expected = ParseLogException.class)
	public void testInvalidLogFile() {
		service.parseFile("src/test/resources/input/invalid_access.log");
	}
	
	@Test
	public void testProcessLogs() {
		String filename = "src/test/resources/input/access.log";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse("2017-01-01.15:00:00", formatter);
		String duration = "hourly";
		Integer threshold = 200;
		
		LocalDateTime testDate = LocalDateTime.parse("2017-01-01.15:01:00", formatter);
		
		Log test = new Log(testDate, "192.168.234.82","request1","200","user_agent1");
		
		when(store.persist(any(Log.class))).thenReturn(test);
		
		service.processLogs(filename, date, duration, threshold);
		
		assert(service.getRequestCount() != null);
		assert(!service.getRequestCount().isEmpty());
		
	}
	
	//Endup didn't using this, but it's a pattern...
	public List<Log> getTestData() {
		List<Log> logs = new ArrayList<Log>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse("2017-01-01.15:00:00", formatter);
		logs.add(new Log(date, "192.168.234.82","request1","200","user_agent1"));
		
		date = LocalDateTime.parse("2017-01-01.15:01:00", formatter);
		logs.add(new Log(date, "192.168.234.83","request2","200","user_agent2"));
		
		date = LocalDateTime.parse("2017-01-01.15:01:01", formatter);
		logs.add(new Log(date, "192.168.234.82","request3","200","user_agent3"));
		
		date = LocalDateTime.parse("2017-01-01.15:02:01", formatter);
		logs.add(new Log(date, "192.168.234.84","request4","200","user_agent4"));
		
		date = LocalDateTime.parse("2017-01-01.15:02:02", formatter);
		logs.add(new Log(date, "192.168.234.82","request5","200","user_agent5"));
		
		return logs;
	}
}
