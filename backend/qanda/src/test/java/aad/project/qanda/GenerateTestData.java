package aad.project.qanda;

import aad.project.qanda.repository.AnswerRepository;
import aad.project.qanda.repository.QuestionRepository;
import aad.project.qanda.repository.UserRepository;
import aad.project.qanda.entity.Answer;
import aad.project.qanda.entity.Question;
import aad.project.qanda.entity.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.lang.System.exit;

@SpringBootApplication
public class GenerateTestData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    static Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(GenerateTestData.class, args);
    }

    @Override
    @Transactional // Ensure all operations are within a transaction
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        generateUsersData(faker);

        generateQuestionsData(faker);

        generateAnswerData(faker);

        Thread.sleep(30000);

        exit(0);
    }

    private void generateAnswerData(Faker faker) {
        List<Question> questions = questionRepository.findAll();

        // Generate test answers for each question
        for (Question question : questions) {
            // Generate random user
            for (int i = 0; i < random.nextInt(5) + 3 ; i++) {
                User user = getRandomUser();

                // Generate random answer
                String answerText = faker.lorem().paragraph();

                // Set current timestamp
                LocalDateTime timestamp = LocalDateTime.now();

                // Create answer entity
                Answer answer = new Answer();
                answer.setAnswer(answerText);
                answer.setTimestamp(timestamp);
                answer.setUser(user);
                answer.setQuestion(question);

                // Save the answer to the database
                answerRepository.save(answer);
            }
        }
        System.out.println("Test answer data generated successfully.");
    }

    private void generateQuestionsData(Faker faker) {
        // Generate test questions
        int numQuestions = 10; // Change this to the desired number of test questions
        for (int i = 0; i < numQuestions; i++) {
            // Generate random user
            User user = getRandomUser();

            // Generate random question
            String questionText = faker.lorem().sentence();

            // Set current timestamp
            LocalDateTime timestamp = LocalDateTime.now();

            // Create question entity
            Question question = new Question();
            question.setQuestion(questionText);
            question.setTimestamp(timestamp);
            question.setUser(user);

            // Save the question to the database
            questionRepository.save(question);
        }
        System.out.println("Test question data generated successfully.");
    }

    private void generateUsersData(Faker faker) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        userRepository.save(user);
        // Generate 10 test users
        for (int i = 0; i < 9; i++) {
            String username = faker.name().username();
            String password = faker.internet().password();

            user = new User();
            user.setUsername(username);
            user.setPassword(password);

            // Save the user to the database
            userRepository.save(user);
        }
        System.out.println("Test user data generated successfully.");
    }

    private User getRandomUser() {
        List<User> users = userRepository.findAll();
        return users.get(random.nextInt(users.size()));
    }
}
