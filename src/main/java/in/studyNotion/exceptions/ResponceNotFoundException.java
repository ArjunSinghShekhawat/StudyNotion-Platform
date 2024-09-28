package in.studyNotion.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponceNotFoundException extends RuntimeException{

    private String resourceField;
    private String resourceName;
    private String message;

    public ResponceNotFoundException(String message,String resourceField,String resourceName){

        super(String.format("%s not found with %s : %s",resourceName,resourceField,message));
        this.message=message;
        this.resourceField=resourceField;
        this.resourceName=resourceName;
    }
}
