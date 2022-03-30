package app.scrumifiedbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class EntityException {
    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;
}
