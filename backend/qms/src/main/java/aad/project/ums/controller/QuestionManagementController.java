package aad.project.ums.controller;

import aad.project.qanda.InvalidSessionException;
import aad.project.qanda.connector.QuestionConnector;
import aad.project.qanda.entity.Question;
import aad.project.qanda.entity.User;
import aad.project.qanda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/qms")
public class QuestionManagementController {

    @Autowired
    private QuestionConnector questionConnector;

    @Autowired
    private UserService userService;

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody Question question) throws InvalidSessionException {
        // Validate user session
        User user = userService.validateUserSession(authorizationHeader);
        // Set user ID to the question
        question.setUser(user);
        // Save the question
        Question createdQuestion = questionConnector.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionConnector.findAll();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String questionId) {
        Optional<Question> optionalQuestion = questionConnector.findById(questionId);
        if (optionalQuestion.isPresent()) {
            return ResponseEntity.ok(optionalQuestion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/questions/user/{userId}")
    public ResponseEntity<List<Question>> getQuestionsByUserId(@PathVariable String userId) {
        List<Question> questions = questionConnector.findByUserUserId(userId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<Question> updateQuestion(@RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable String questionId,
                                                   @RequestBody Question questionDetails) throws InvalidSessionException {
        // Validate user session
        userService.validateUserSession(authorizationHeader);
        Optional<Question> optionalQuestion = questionConnector.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = optionalQuestion.get();
            existingQuestion.setQuestion(questionDetails.getQuestion());
            Question updatedQuestion = questionConnector.save(existingQuestion);
            return ResponseEntity.ok(updatedQuestion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable String questionId) throws InvalidSessionException {
        // Validate user session
        userService.validateUserSession(authorizationHeader);
        Optional<Question> optionalQuestion = questionConnector.findById(questionId);
        if (optionalQuestion.isPresent()) {
            questionConnector.deleteById(questionId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
