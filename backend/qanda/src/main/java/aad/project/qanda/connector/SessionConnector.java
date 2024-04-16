package aad.project.qanda.connector;

import aad.project.qanda.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessionConnector extends JpaRepository<Session, String> {
}
