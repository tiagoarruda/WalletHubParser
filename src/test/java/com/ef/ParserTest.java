package com.ef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import com.ef.exception.LogNotFoundException;
import com.ef.service.ParseService;

public class ParserTest {
	
	ParseService service;
	Parser parser;

	@Before
	public void setUp() throws Exception {
		service = mock(ParseService.class);
		parser = new Parser();
		parser.setService(service);
	}

	@Test
	public void testParse() {
		String[] args = {"--accesslog=access.log", "--startDate=2017-01-01.15:00:00", "--duration=hourly", "--threshold=200"};
		try {
			this.parser.run(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unexpected exception at happy path test");
		}
		
		assertEquals(this.parser.getAccesslog(), "access.log");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse("2017-01-01.15:00:00", formatter);
		assert(this.parser.getDate().equals(date));
		assertEquals(this.parser.getDuration(), "hourly");
		assertEquals(this.parser.getThreshold().toString(), "200");
	}
	
	@Test
	public void testMissingArgs() {
		String[] args = {"--startDate=2017-01-01.15:00:00", "--duration=hourly", "--threshold=200"};
		
		doThrow(new LogNotFoundException()).when(service).processLogs(anyString(), any(), anyString(), anyInt());
		
		try {
			this.parser.run(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("The exception should be catched");
		}
	}

}
