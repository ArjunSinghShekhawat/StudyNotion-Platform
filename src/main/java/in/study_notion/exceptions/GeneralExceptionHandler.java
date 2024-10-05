package in.study_notion.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralExceptionHandler extends RuntimeException{

    private String message;
    private Throwable cause;

    public GeneralExceptionHandler(String message,Throwable cause){
        super(message,cause);
        this.cause = cause;
        this.message = message;
    }

}

