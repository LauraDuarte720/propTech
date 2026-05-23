package co.edu.uniquindio.com.proptech.exceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ══════════════════════════════════════════════
    // 404 NOT FOUND
    // ══════════════════════════════════════════════

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    // ══════════════════════════════════════════════
    // 409 CONFLICT — generales
    // ══════════════════════════════════════════════

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    // ══════════════════════════════════════════════
    // 409 CONFLICT — específicos con data extra
    // ══════════════════════════════════════════════

    @ExceptionHandler(VipVisitDisplacementException.class)
    public ResponseEntity<ErrorResponse> handleVipDisplacement(VipVisitDisplacementException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        response.getDetails().put("displacedVisitId", ex.getDisplacedVisitId());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ZoneChangeConflictException.class)
    public ResponseEntity<ErrorResponse> handleZoneConflict(ZoneChangeConflictException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        response.getDetails().put("affectedProperties", ex.getAffectedProperties());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // ══════════════════════════════════════════════
    // 401 UNAUTHORIZED
    // ══════════════════════════════════════════════

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    // ══════════════════════════════════════════════
    // 409 — SupportRequestNotCancellable (extiende RuntimeException directo)
    // ══════════════════════════════════════════════

    @ExceptionHandler(SupportRequestNotCancellableException.class)
    public ResponseEntity<ErrorResponse> handleSupportRequestNotCancellable(SupportRequestNotCancellableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    // ══════════════════════════════════════════════
    // 400 BAD REQUEST — validaciones @Validated
    // ══════════════════════════════════════════════

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            response.getDetails().put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ══════════════════════════════════════════════
    // 400 BAD REQUEST — IllegalArgumentException
    // ══════════════════════════════════════════════

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    // ══════════════════════════════════════════════
    // 500 INTERNAL SERVER ERROR — cualquier otra
    // ══════════════════════════════════════════════

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error: " + ex.getMessage()));
    }


}