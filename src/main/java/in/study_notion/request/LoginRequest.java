package in.study_notion.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "please enter valid email !")
    private String email;

    @NotBlank(message = "empty and blank password does not accept")
    private String password;
}
