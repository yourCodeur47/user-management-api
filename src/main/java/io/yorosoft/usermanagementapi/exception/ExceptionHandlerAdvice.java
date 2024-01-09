package io.yorosoft.usermanagementapi.exception;

import io.yorosoft.usermanagementapi.utils.ResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResultDTO handleObjectNotFoundException(ObjectNotFoundException ex) {
        return new ResultDTO(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex);
    }

    /**
     * This handles invalid inputs.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResultDTO handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new ResultDTO(false, HttpStatus.BAD_REQUEST.value(), "Provided arguments are invalid, see data for details.", map);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResultDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResultDTO(false, HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultDTO handleAuthenticationException(Exception ex) {
        return new ResultDTO(false, HttpStatus.UNAUTHORIZED.value(), "username or password is incorrect.", ex);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultDTO handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return new ResultDTO(false, HttpStatus.UNAUTHORIZED.value(), "Login credentials are missing.", ex);
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResultDTO handleAccountStatusException(AccountStatusException ex) {
        return new ResultDTO(false, HttpStatus.UNAUTHORIZED.value(), "User account is abnormal.", ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResultDTO handleAccessDeniedException(AccessDeniedException ex) {
        return new ResultDTO(false, HttpStatus.FORBIDDEN.value(), "No permission.", ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResultDTO handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResultDTO(false, HttpStatus.NOT_FOUND.value(), "This API endpoint is not found.", ex);
    }

    /**
     * Fallback handles any unhandled exceptions.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResultDTO handleOtherException(Exception ex) {
        return new ResultDTO(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "A server internal error occurs.", ex);
    }

}
