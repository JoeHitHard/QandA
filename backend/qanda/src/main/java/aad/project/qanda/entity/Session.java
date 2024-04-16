package aad.project.qanda.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    private String sessionId;

    @ManyToOne
    private User username;

    public Session(String sessionId, User username) {
        this.sessionId = sessionId;
        this.username = username;
    }

    public Session() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }
}
