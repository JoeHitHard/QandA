package aad.project.qanda.connector;

import aad.project.qanda.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerConnector extends JpaRepository<Answer, String> {
}
