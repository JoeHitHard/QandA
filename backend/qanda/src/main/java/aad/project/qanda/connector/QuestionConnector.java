package aad.project.qanda.connector;

import aad.project.qanda.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionConnector extends JpaRepository<Question, String> {
    List<Question> findByQuestionContaining(String keyWord);
}
