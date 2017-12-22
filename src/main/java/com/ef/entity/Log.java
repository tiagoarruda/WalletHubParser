package com.ef.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
	private LocalDateTime date;
	private String IP;
	private String request;
	private String status;
	private String userAgent;
	
	
	
	public Log() {
		super();
	}
	public Log(LocalDateTime date, String iP, String request, String status, String userAgent) {
		super();
		this.date = date;
		IP = iP;
		this.request = request;
		this.status = status;
		this.userAgent = userAgent;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		this.date = LocalDateTime.parse(date, formatter);
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.date);
		sb.append("|");
		sb.append(this.IP);
		sb.append("|");
		sb.append(this.request);
		sb.append("|");
		sb.append(this.status);
		sb.append("|");
		sb.append(this.userAgent);
		return sb.toString();
	}
	
}
