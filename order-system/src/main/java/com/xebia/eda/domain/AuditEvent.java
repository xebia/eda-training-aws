package com.xebia.eda.domain;

import java.util.UUID;

public class AuditEvent {

    private final String id;
    private final String message;

    public AuditEvent(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public AuditEvent(String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public static AuditEvent of(String message){
     return new AuditEvent(message);
    }
}
