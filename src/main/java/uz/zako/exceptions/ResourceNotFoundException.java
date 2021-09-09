package uz.zako.exceptions;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceNotFoundException  extends RuntimeException{
    private String message;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
