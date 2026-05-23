package co.edu.uniquindio.com.proptech.exceptions;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> details;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = new HashMap<>();
    }
}