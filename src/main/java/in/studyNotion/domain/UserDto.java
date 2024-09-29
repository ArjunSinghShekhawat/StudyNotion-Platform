package in.studyNotion.domain;

import in.studyNotion.enums.AccountTypes;
import in.studyNotion.models.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class UserDto {

    @Id
    private int id;

    private String firstName;

    private String lastName;

    @NonNull
    private String email;

    private AccountTypes accountTypes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String phoneNumber;

    private String password;

    private ProfileDto profileDto;

    private String token;

    private LocalTime resetPasswordExpire;

    private String imageUrl;

}
