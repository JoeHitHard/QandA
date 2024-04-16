package aad.project.qanda.repository;

import aad.project.qanda.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByQuestionContaining(String keyWord);
    List<Question> findByUserUserId(String userId);
}
