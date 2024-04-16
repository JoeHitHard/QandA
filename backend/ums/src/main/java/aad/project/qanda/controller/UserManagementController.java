package aad.project.qanda.controller;

import aad.project.qanda.InvalidSessionException;
import aad.project.qanda.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ums")
public class UserManagementController {
    private static final Logger LOG = LoggerFactory.getLogger(UserManagementController.class);
    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity getUserInSession(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidSessionException {
        return ResponseEntity.ok(userService.getUser(authorizationHeader));
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody String logInData) throws InvalidSessionException {
        try {
            JSONObject logInDetails = new JSONObject(logInData);
            return ResponseEntity.ok(userService.login(logInDetails.getString("username"), logInDetails.getString("password")));
        } catch (Exception e) {
            LOG.error("user login Failed, {}", e.getMessage(), e);
            throw e;
        }
    }

    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<Object> handleInvalidSessionException(InvalidSessionException ex) {
        LOG.error("user login Failed, {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
