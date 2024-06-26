package aad.project.qanda.controller;

import aad.project.qanda.InvalidSessionException;
import aad.project.qanda.entity.Question;
import aad.project.qanda.repository.AnswerRepository;
import aad.project.qanda.entity.Answer;
import aad.project.qanda.entity.User;
import aad.project.qanda.repository.QuestionRepository;
import aad.project.qanda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ams")
public class AnswerManagementController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/answers/{questionId}")
    public ResponseEntity<Answer> createAnswer(@PathVariable String questionId, @RequestHeader("Authorization") String authorizationHeader, @RequestBody Answer answer) throws InvalidSessionException {
        // Validate user session
        User user = userService.validateUserSession(authorizationHeader);
        Optional<Question> byId = questionRepository.findById(questionId);
        if (byId.isEmpty()) {
            throw new RuntimeException("Question not found");
        }
        answer.setQuestion(byId.get());
        // Set user ID to the answer
        answer.setUser(user);
        // Set current timestamp
        answer.setTimestamp(LocalDateTime.now());
        // Save the answer
        Answer createdAnswer = answerRepository.save(answer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    @GetMapping("/answers")
    public ResponseEntity<List<Answer>> getAllAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable String questionId) {
        List<Answer> answers = answerRepository.findByQuestionQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/user/{userId}")
    public ResponseEntity<List<Answer>> getAnswersByUserId(@PathVariable String userId) {
        if (userId.equals("all")) {
            List<Answer> answers = answerRepository.findAll();
            return ResponseEntity.ok(answers);
        }
        List<Answer> answers = answerRepository.findByUserUserId(userId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable String answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            return ResponseEntity.ok(optionalAnswer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/answers/{answerId}")
    public ResponseEntity<Answer> updateAnswer(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String answerId, @RequestBody Answer answerDetails) throws InvalidSessionException {
        // Validate user session
        userService.validateUserSession(authorizationHeader);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer existingAnswer = optionalAnswer.get();
            existingAnswer.setAnswer(answerDetails.getAnswer());
            // Update other fields if necessary
            Answer updatedAnswer = answerRepository.save(existingAnswer);
            return ResponseEntity.ok(updatedAnswer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String answerId) throws InvalidSessionException {
        // Validate user session
        userService.validateUserSession(authorizationHeader);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            answerRepository.deleteById(answerId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

