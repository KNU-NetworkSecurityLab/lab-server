package spring.labserver.error;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException {
    private final String msg;
    private final HttpStatus HttpStatus;
    private final ZonedDateTime timestamp;

}
