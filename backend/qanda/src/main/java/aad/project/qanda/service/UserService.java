package aad.project.qanda.service;

import aad.project.qanda.InvalidSessionException;
import aad.project.qanda.connector.SessionConnector;
import aad.project.qanda.connector.UserConnector;
import aad.project.qanda.entity.Session;
import aad.project.qanda.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserConnector userConnector;

    @Autowired
    SessionConnector sessionConnector;

    public Session login(String userName, String password) throws InvalidSessionException {
        Optional<User> user = userConnector.findByUsername(userName);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                Session Session = new Session(UUID.randomUUID().toString(), user.get());
                sessionConnector.save(Session);
                return Session;
            }
            throw new InvalidSessionException("Userid " + userName + " password is not Correct");
        }
        throw new InvalidSessionException("Userid " + userName + " not Found");
    }

    public Optional<User> getUser(String sessionId) throws InvalidSessionException {
        Optional<Session> token = sessionConnector.findById(sessionId);
        if (token.isPresent()) {
            return Optional.of(token.get().getUsername());
        }
        throw new InvalidSessionException("Invalid Session ID");
    }

    public User validateUserSession(String sessionId) throws InvalidSessionException {
        Optional<User> user = getUser(sessionId);
        if (user.isEmpty()) {
            throw new InvalidSessionException("Invalid session");
        }
        return user.get();
    }
}
