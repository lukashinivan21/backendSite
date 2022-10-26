package backends.backendsite.exceptionsHandler;

import backends.backendsite.exceptionsHandler.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class including methods for creating responses to exceptions
 */
@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserExceptions() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("User with this id not found");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<Object> notAccess() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("You haven't access to this information");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Object> wrongPassword() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Check entered password");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyListException.class)
    public ResponseEntity<Object> emptyList() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Empty list without info");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadInputDataException.class)
    public ResponseEntity<Object> badData() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Check data your entered");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<Object> adsNotFound() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Ads with this id not found");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectAdsIdException.class)
    public ResponseEntity<Object> incorrectAdsId() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Ads with this id doesn't exist");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> commentNotFound() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Comment with this id not found");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAccessActionException.class)
    public ResponseEntity<Object> notAction() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("You haven't access to this action");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Object> imageNotFound() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Image with this id not found");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<Object> serverError() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Fatal server error");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> existedUser() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("User with this email already exists. Check input email");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Object> notLogin() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Login is forbidden. Check input username and password");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CommentNotBelongAdException.class)
    public ResponseEntity<Object> commentNotBelong() {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Comment with this id doesn't belong to ads with this id");
        response.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
