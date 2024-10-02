package in.study_notion.exceptions;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException{

    private String message;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
