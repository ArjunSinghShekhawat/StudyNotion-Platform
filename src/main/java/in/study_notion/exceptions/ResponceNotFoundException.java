package in.study_notion.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponceNotFoundException extends RuntimeException{

    private String resourceName;
    private String fieldName;
    private String fieldValue;

    public  ResponceNotFoundException(String resourceName,String fieldName,String fieldValue){
        super(String.format("%s Not Found With %s : %s ",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }
}
