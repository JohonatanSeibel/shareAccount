package br.com.shareaccount.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;

@Slf4j
@RestControllerAdvice
public class ShareAccountExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ConvertibleException.class })
    public ResponseEntity<Object> handleConvertibleException(ConvertibleException ex) {
        return errorResponse(ex.getStatus(), ex.getMessage());
    }

    private ResponseEntity<Object> errorResponse(HttpStatus statusCode, String msg) {
        var errorResponse = new ErrorResponse(msg);
        log.error("Erro: {}", errorResponse);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}

@Data
@AllArgsConstructor
@JsonInclude(content = JsonInclude.Include.NON_NULL)
class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
}
