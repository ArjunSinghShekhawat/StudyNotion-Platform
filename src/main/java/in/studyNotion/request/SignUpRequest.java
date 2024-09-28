package in.studyNotion.request;

import in.studyNotion.enums.AccountTypes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "empty and blank first name does not accept")
    private String firstName;

    @NotBlank(message = "empty and blank last name does not accept")
    private String lastName;

    @Email(message = "please enter valid email !")
    private String email;

    @NotNull(message = "null account type does not accept !")
    private AccountTypes accountTypes;

    private String phoneNumber;

    @NotBlank(message = "empty and blank password does not accept")
    private String password;
}
