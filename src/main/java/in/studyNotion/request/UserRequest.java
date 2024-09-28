package in.studyNotion.request;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserRequest {

    private String firstName;

    private String lastName;

    private LocalDateTime updatedAt;

}
