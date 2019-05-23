package com.ejennco.eventtracker.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ejennco.eventtracker.EventConverter;
import com.ejennco.eventtracker.dto.LogEvent;
import com.ejennco.eventtracker.model.Event;
import com.ejennco.eventtrcker.dao.EventDao;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogService implements ILogService {
	private static Logger log = LoggerFactory.getLogger(LogService.class);
    private final ObjectMapper mapper;
    private final Connection connection;
    private final EventConverter eventConverter;
	private Map<String, LogEvent> startedMap = new ConcurrentHashMap<>();
	private Map<String, LogEvent> finishedMap = new ConcurrentHashMap<>();

	@Autowired
    public LogService(ObjectMapper objectMapper, Connection connection, EventConverter eventConverter) {
        this.mapper = objectMapper;
        this.connection = connection;
        this.eventConverter = eventConverter;
    }

	@Override
	public void parseJsonFile(String filePath) throws IOException {
		log.info("Processing json file {}", filePath);
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			Files.lines(Paths.get(filePath)).parallel().filter(StringUtils::hasText)
					.forEach(line -> {
						try {
							convertToLogEvent(line);
						} catch (IOException e) {
							log.error("Failure converting line {}", line, e);
						}
					});
			processData(startedMap.keySet());
			log.info("Completed processing json file {}, closing...", filePath);
		} catch (IOException e) {
			log.error("Failure reading file {}", filePath, e);
            throw e;
		}
	}

	private LogEvent convertToLogEvent(String line) throws IOException {
		log.info("Starting conversion of  {} to logevent", line);
		try {
			LogEvent logEvent = mapper.readValue(line, LogEvent.class);
			if (logEvent.getState() == LogEvent.State.FINISHED) {
				finishedMap.put(logEvent.getId(), logEvent);
			} else {
				startedMap.put(logEvent.getId(), logEvent);
			}
			log.info("Converted event id {} to logevent", logEvent.getId());
			return logEvent;
		} catch (IOException e) {
			log.error("Failure converting line:  {} to event", line, e);
            throw e;
		}
	}

	/**
	 * Takes event ids and finds corresponding start and finish events if found
	 * saves resulting event
	 *
	 * @param ids
	 */
	private void processData(Set<String> ids) {
		log.debug("Starting processData");
		try (EventDao eventDao = new EventDao(connection)) {
			for (String id : ids) {
				LogEvent startEvent = startedMap.get(id);
				LogEvent finishEvent = finishedMap.get(id);
				if (startEvent != null && finishEvent != null) {
					log.debug("Calling converter for id {} ", id);
					Event event = eventConverter.EventDTOToEvent(startEvent, finishEvent);
					log.debug("Conversion complete, saving data");
					eventDao.save(event);
					log.debug("Save Complete");
				} else {
					log.error("Failure processing event {}", id);
				}
			}
		}
	}

}
