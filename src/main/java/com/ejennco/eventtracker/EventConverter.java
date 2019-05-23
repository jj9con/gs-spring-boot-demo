package com.ejennco.eventtracker;

import org.springframework.stereotype.Component;

import com.ejennco.eventtracker.dto.LogEvent;
import com.ejennco.eventtracker.model.Event;


@Component
public class EventConverter {
    /**
     * Takes logEvent start and finish objects and converts to EventDAO
     *
     * @param startEvent
     * @param finishEvent
     * @return Event object
     */
    public Event EventDTOToEvent(LogEvent startEvent, LogEvent finishEvent) {
        Long duration =  finishEvent.getTimestamp() - startEvent.getTimestamp();
        boolean isAlert = duration > 4;
        return new Event(startEvent.getId(), duration, startEvent.getType(), startEvent.getHost(), isAlert);
    }
}
