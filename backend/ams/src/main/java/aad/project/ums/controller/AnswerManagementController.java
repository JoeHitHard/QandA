package aad.project.ums.controller;

import aad.project.qanda.InvalidSessionException;
import aad.project.qanda.connector.AnswerConnector;
import aad.project.qanda.entity.Answer;
import aad.project.qanda.entity.User;
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
    private AnswerConnector answerConnector;

    @Autowired
    private UserService userService;

    @PostMapping("/answers")
    public ResponseEntity<Answer> createAnswer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Answer answer) throws InvalidSessionException {
        // Validate user session
        User user = userService.validateUserSession(authorizationHeader);
        // Set user ID to the answer
        answer.setUser(user);
        // Set current timestamp
        answer.setTimestamp(LocalDateTime.now());
        // Save the answer
        Answer createdAnswer = answerConnector.save(answer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    @GetMapping("/answers")
    public ResponseEntity<List<Answer>> getAllAnswers() {
        List<Answer> answers = answerConnector.findAll();
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable String questionId) {
        List<Answer> answers = answerConnector.findByQuestionQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/user/{userId}")
    public ResponseEntity<List<Answer>> getAnswersByUserId(@PathVariable String userId) {
        List<Answer> answers = answerConnector.findByUserUserId(userId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable String answerId) {
        Optional<Answer> optionalAnswer = answerConnector.findById(answerId);
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
        Optional<Answer> optionalAnswer = answerConnector.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer existingAnswer = optionalAnswer.get();
            existingAnswer.setAnswer(answerDetails.getAnswer());
            // Update other fields if necessary
            Answer updatedAnswer = answerConnector.save(existingAnswer);
            return ResponseEntity.ok(updatedAnswer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String answerId) throws InvalidSessionException {
        // Validate user session
        userService.validateUserSession(authorizationHeader);
        Optional<Answer> optionalAnswer = answerConnector.findById(answerId);
        if (optionalAnswer.isPresent()) {
            answerConnector.deleteById(answerId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

