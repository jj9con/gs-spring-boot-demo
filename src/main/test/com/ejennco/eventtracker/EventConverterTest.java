package com.ejennco.eventtracker;

import org.junit.Test;

import com.ejennco.eventtracker.dto.LogEvent;
import com.ejennco.eventtracker.model.Event;

import static org.junit.Assert.*;

public class EventConverterTest {
    private static final String ID = "123a";
    private EventConverter eventConverter = new EventConverter();

    @Test
    public void testEventDTOToEvent_NonAlertEvent() {
        LogEvent start = new LogEvent(ID, LogEvent.State.STARTED, 123L);
        LogEvent finish = new LogEvent(ID, LogEvent.State.FINISHED, 124L);

        Event event = eventConverter.EventDTOToEvent(start, finish);
        assertFalse("Event should not be returned as alert", event.isAlert());
    }

    @Test
    public void testEventDTOToEvent_AlertEvent() {
        LogEvent start = new LogEvent(ID, LogEvent.State.STARTED, 123L);
        LogEvent finish = new LogEvent(ID, LogEvent.State.FINISHED, 128L);

        Event event = eventConverter.EventDTOToEvent(start, finish);
        assertTrue("Event should be returned as alert", event.isAlert());
    }
}