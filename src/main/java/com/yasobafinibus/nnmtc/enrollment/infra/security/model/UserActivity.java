package com.yasobafinibus.nnmtc.enrollment.infra.security.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "logs")
public class UserActivity extends AbstractAuditEntity implements Serializable {


    @Column(name = "USER_NAME", nullable = false, length = 30)
    private String userName = "SYSTEM";


    @Column(name = "EVENT_ID", nullable = false)
    private UUID eventId = UUID.randomUUID();


    @Column(name = "EVENT_DATE", nullable = false, length = 50)
    private LocalDateTime eventDate = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "EVENT_TYPE", length = 10)
    private EventType eventType;


    @Enumerated(value = EnumType.STRING)
    @Column(name = "EVENT_OUTCOME", length = 10)
    private EventOutcome eventOutcome = EventOutcome.NONE;

    @Column(name = "LOGGER", nullable = false, length = 1000)
    private String logger;

    @Column(name = "MESSAGE", nullable = false, length = 1000)
    private String message;

    public String getUserName() {
        return this.userName;
    }

    public UUID getEventId() {
        return this.eventId;
    }

    public LocalDateTime getEventDate() {
        return this.eventDate;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public EventOutcome getEventOutcome() {
        return this.eventOutcome;
    }

    public String getLogger() {
        return this.logger;
    }

    public String getMessage() {
        return this.message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setEventOutcome(EventOutcome eventOutcome) {
        this.eventOutcome = eventOutcome;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum EventType {

        UPDATE, DELETE, CREATE, REFRESH, LOGIN, LOGOUT
    }

    public enum EventOutcome {
        SUCCESS, FAILURE, PENDING, NONE
    }


    public static final class UserActivityBuilder {
        private String userName;
        private EventType eventType;
        private EventOutcome eventOutcome;
        private String logger;
        private String message;

        private UserActivityBuilder() {
        }

        public static UserActivityBuilder
        anUserActivity() {
            return new UserActivityBuilder();
        }

        public UserActivityBuilder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserActivityBuilder withEventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public UserActivityBuilder withEventOutcome(EventOutcome eventOutcome) {
            this.eventOutcome = eventOutcome;
            return this;
        }

        public UserActivityBuilder withLogger(String logger) {
            this.logger = logger;
            return this;
        }

        public UserActivityBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public UserActivity build() {
            UserActivity userActivity = new UserActivity();
            userActivity.setUserName(userName);
            userActivity.setEventType(eventType);
            userActivity.setEventOutcome(eventOutcome);
            userActivity.setLogger(logger);
            userActivity.setMessage(message);
            return userActivity;
        }
    }
}
