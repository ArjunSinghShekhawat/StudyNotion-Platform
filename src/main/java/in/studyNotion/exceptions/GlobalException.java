package in.studyNotion.exceptions;

import in.studyNotion.responce.ResponceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResponceNotFoundException.class)
    public ResponseEntity<ResponceApi>resourceNotFoundExceptionHandler(ResponceNotFoundException rs){
        String message  = rs.getMessage();
        ResponceApi responceApi = new ResponceApi(message,false);

        return new ResponseEntity<>(responceApi, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String,String>resp = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String defaultMessage = error.getDefaultMessage();
            resp.put(fieldName,defaultMessage);
        });

        return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
    }
}
