package in.study_notion.responce;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponce {

    private boolean status;
    private String message;
    private String jwt;

}
