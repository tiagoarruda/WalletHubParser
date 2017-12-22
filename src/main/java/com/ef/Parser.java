package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ef.exception.LogNotFoundException;
import com.ef.exception.ParseLogException;
import com.ef.service.ParseService;

@SpringBootApplication
public class Parser implements CommandLineRunner {
	
	private final static Logger logger = Logger.getLogger(Parser.class.getName());
	
	@Autowired
	private ParseService service;
	
	private String accesslog;
	private LocalDateTime date;
	private String duration;
	private Integer threshold;

	public static void main(String[] args) {
		logger.log(Level.INFO, "Test start.");
		
		SpringApplication.run(Parser.class, args);

	}
	
	@Override
	public void run(String... arg) throws Exception {
		try {
			
			parseArgs(arg);
			
			service.processLogs(accesslog,date, duration, threshold);
			
		} catch (LogNotFoundException ex) {
			System.out.println("Log file not found.");
			logger.log(Level.SEVERE, "Log file not found.");
			ex.printStackTrace();
		} catch (ParseLogException ex) {
			logger.log(Level.SEVERE, "A error occurred while parsing the log file.");
			ex.printStackTrace();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unknow exception prevented succesfull code execution.");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Initiates the command line parser with the arguments.
	 * @param args
	 */
	private void parseArgs(String[] args) {
		Options options = new Options();
		options.addOption("a", "accesslog", true, "The name of the log file to be parsed");
		options.addOption("s", "startDate", true, "The start date to query the log collection");
		options.addOption("d", "duration", true, "The duration to query for ip in the logs. The reference to the duration is the start date.");
		options.addOption("t", "threshold", true, "The start date to query the log collection");
		
		CommandLineParser cmdParser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = cmdParser.parse( options, args);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Error while parsing arguments.");
			e.printStackTrace();
		}
		
		this.accesslog = cmd.getOptionValue("accesslog");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
		this.date = LocalDateTime.parse(cmd.getOptionValue("startDate"), formatter);
		this.duration = cmd.getOptionValue("duration");
		this.threshold = Integer.parseInt(cmd.getOptionValue("threshold"));
		
	}

	
	public void setService(ParseService service) {
		this.service = service;
	}

	public String getAccesslog() {
		return accesslog;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getDuration() {
		return duration;
	}

	public Integer getThreshold() {
		return threshold;
	}
	
	
}
