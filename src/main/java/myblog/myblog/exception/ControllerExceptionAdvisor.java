package myblog.myblog.exception;

import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.exception.custom_exeption.CommentException;
import myblog.myblog.exception.custom_exeption.MemberException;
import myblog.myblog.exception.custom_exeption.PostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ControllerExceptionAdvisor {

    /**
     * 회원가입 Valid 예외
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String signValidException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage() + "\n");
        }

        return builder.toString();
    }

    /**
     * MemberException, PostException, CommentException 예외
     */
    @ExceptionHandler({MemberException.class, PostException.class, CommentException.class})
    public ResponseEntity memberExceptionHandler(Exception exception) {
        String message = exception.getMessage();
        BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(message);
        return new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
