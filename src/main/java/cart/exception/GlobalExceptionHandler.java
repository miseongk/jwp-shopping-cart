package cart.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValid(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSuchElement(final NoSuchElementException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ExistProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExistProduct(final ExistProductException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthorization(final AuthorizationException e) {
        return new ErrorResponse(e.getMessage());
    }

}
