package com.ejennco.eventtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogEvent {
    private String id;
    private  State state;
    private long timestamp;
    private  String type;
    private String host;

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public LogEvent() {
    }

    public LogEvent(String id, State state, long timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public enum State {
        @JsonProperty("STARTED")
        STARTED,
        @JsonProperty("FINISHED")
        FINISHED
    }

}
