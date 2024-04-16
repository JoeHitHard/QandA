package aad.project.qanda.connector;

import aad.project.qanda.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserConnector extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

}
