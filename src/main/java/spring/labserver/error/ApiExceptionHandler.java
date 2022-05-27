package spring.labserver.error;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import spring.labserver.error.exception.UserAlreadyExistException;
import spring.labserver.error.exception.UserNotExistException;
import spring.labserver.error.exception.UserNullException;

// Controller 에서 발생하는 error를 처리하는 클래스
@ControllerAdvice
public class ApiExceptionHandler {

    // 400
    // userId가 존재하지 않을 때
    @ExceptionHandler(value = {UserNotExistException.class})
    public ResponseEntity<Object> handlerUserNotExistException(UserNotExistException e) {
        
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
            "Not Existing ID",
            httpStatus,
            ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    // 400
    // userId가 이미 존재할 때
    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> handlerUserAlreadyExistException(UserAlreadyExistException e) {
        
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
            "Duplicated userId",
            httpStatus,
            ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    // 400
    // 데이터에 Null이 포함되어 있을 때
    @ExceptionHandler(value = {UserNullException.class})
    public ResponseEntity<Object> handlerUserNullException(UserNullException e) {
        
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
            "Null included in data",
            httpStatus,
            ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }
}
