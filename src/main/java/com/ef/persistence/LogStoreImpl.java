package com.ef.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ef.entity.Log;

@Repository
public class LogStoreImpl implements LogStore {

	private final static Logger logger = Logger.getLogger(LogStoreImpl.class.getName());

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Value("${request.ip.lock.message}")
	private String lockMessage;

	@Override
	public Log persist(Log log) {
		try {
			jdbcTemplate.update(conn->{
				PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO log(ip, date, request, status, user_agent) VALUES (?,?,?,?,?)");
				ps.setString(1, log.getIP());
				ps.setString(2, log.getDate().toString());
				ps.setString(3, log.getRequest());
				ps.setInt(4, Integer.parseInt(log.getStatus()));
				ps.setString(5, log.getUserAgent());
				return ps;
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE, "A error prevented a log insertion");
			e.printStackTrace();
		}
		return log;
	}

	@Override
	public void persist(List<Log> logs) {
		try {
			jdbcTemplate.batchUpdate("INSERT INTO log(ip, date, request, status, user_agent) VALUES (?,?,?,?,?)",
					new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					Log log = logs.get(index);
					ps.setString(1, log.getIP());
					ps.setString(2, log.getDate().toString());
					ps.setString(3, log.getRequest());
					ps.setInt(4, Integer.parseInt(log.getStatus()));
					ps.setString(5, log.getUserAgent());
				}

				@Override
				public int getBatchSize() {
					return logs.size();
				}
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE, "A error prevented a log insertion");
			e.printStackTrace();
		}
	}

	@Override
	public void persistLockMessage(String ip, Integer requests, LocalDateTime initialDate, LocalDateTime endingDate,
			Integer threshold) {
		try {
			jdbcTemplate.update(conn->{
				PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO locks(ip, message, request_quantity, initialDate, endingDate, requests_threshold) VALUES (?,?,?,?,?,?)");
				ps.setString(1, ip);
				ps.setString(2, lockMessage);
				ps.setInt(3, requests);
				ps.setString(4, initialDate.toString());
				ps.setString(5, endingDate.toString());
				ps.setInt(6, threshold);
				return ps;
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE, "A error prevented a ip lock message insertion");
			e.printStackTrace();
		}
	}



}
