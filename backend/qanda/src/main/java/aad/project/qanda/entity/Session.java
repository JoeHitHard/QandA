package aad.project.qanda.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(36)")
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
