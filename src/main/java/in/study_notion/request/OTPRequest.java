package in.study_notion.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OTPRequest {

    @NotBlank(message = "email is required !")
    private String email;
}
