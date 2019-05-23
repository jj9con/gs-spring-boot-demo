package com.ejennco.eventtrcker.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ejennco.eventtracker.model.Event;

@Component
public class EventDao implements AutoCloseable {
    private static Logger log = LoggerFactory.getLogger(EventDao.class);

    private Connection connection;
    private final static String sql = "INSERT INTO event (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";

    public EventDao(Connection connection) {
    	log.debug("Starting EventDAO");
        this.connection = connection;
    }

    public Boolean save(Event event) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, event.getId());
            statement.setLong(2, event.getDuration());
            statement.setString(3, event.getType());
            statement.setString(4, event.getHost());
            statement.setBoolean(5, event.isAlert());
            log.info("executing statement: {} ",statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("Failure saving event {} ",event.getId(),  e);
            return false;
        }
    }

    @Override
    public void close() {
    	log.debug("Closing connection");
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Failure closing database connection",  e);
        }
    }
}
