package in.studyNotion.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordRequest {

    private String email;
    private String password;
    private String confirmPassword;
}

