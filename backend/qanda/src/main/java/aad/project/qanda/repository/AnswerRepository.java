package aad.project.qanda.repository;

import aad.project.qanda.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
    List<Answer> findByQuestionQuestionId(String questionId);
    List<Answer> findByUserUserId(String answerId);
}
