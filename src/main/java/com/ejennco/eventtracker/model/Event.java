package com.ejennco.eventtracker.model;
public class Event {

    private String id;
    private final Long duration;
    private String type;
    private String host;
    private final boolean alert;

    public Event(String id, Long duration, String type, String host, boolean alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }

    public String getId() {
        return id;
    }

    public Long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return alert;
    }

}

