package com.ef.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ef.entity.Duration;
import com.ef.entity.Log;
import com.ef.exception.LogNotFoundException;
import com.ef.exception.ParseLogException;
import com.ef.persistence.LogStore;

@Service
public class ParseServiceImpl implements ParseService {

	private final static Logger logger = Logger.getLogger(ParseServiceImpl.class.getName());

	@Autowired
	private LogStore store; 
	private List<Log> logs = null;
	private Map<String, Integer> requestCount;

	public void processLogs(String filename, LocalDateTime startDate, String duration, Integer threshold) {
		requestCount = new HashMap<String, Integer>();
		parseFile(filename);

		Duration d = Duration.valueOf(duration.toUpperCase());
		LocalDateTime endingDate = d.getEndingDate(startDate);

		//Insert log and count ip requests
		logs.stream().parallel().forEach(log->{
			this.store.persist(log);

			//Filtering
			if (startDate.isBefore(log.getDate())
					&& endingDate.isAfter(log.getDate())) {
				requestCount.put(log.getIP(), requestCount.containsKey(log.getIP()) ? requestCount.get(log.getIP()) + 1 : 1);
			}
		});

		logger.info("List of IP's that sent requests above the allowed threshold:");
		requestCount.keySet().stream().parallel().forEach(key->{
			if (requestCount.get(key) >= threshold) {
				System.out.println(key);
				store.persistLockMessage(key, requestCount.get(key), startDate, endingDate, threshold);
			}
		});

		/*
		 * Here I wanted to test the batchUpdaet performance against the stream parallel processing.
		 * The parallel processing performance was better running at my machine. 
		 * store.persist(logs);
		 */
	}

	public void parseFile(String filename) {
		this.logs = new ArrayList<Log>();
		File logFile = new File(filename);
		if (!logFile.exists()) {
			throw new LogNotFoundException();
		}

		Scanner scan;
		try {
			scan = new Scanner(logFile);

			scan.useDelimiter("[\\||\\n]");

			while (scan.hasNext()) {
				Log log = new Log();
				log.setDate(scan.next());
				log.setIP(scan.next());
				log.setRequest(scan.next());
				log.setStatus(scan.next());
				log.setUserAgent(scan.next());

				logs.add(log);
			}

			scan.close();
			logger.log(Level.INFO, "Parsed " + logs.size() + " lines.");
		} catch (FileNotFoundException e) {
			throw new LogNotFoundException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseLogException();
		}
	}

	public void setStore(LogStore store) {
		this.store = store;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public Map<String, Integer> getRequestCount() {
		return requestCount;
	}
	
	
	
}
