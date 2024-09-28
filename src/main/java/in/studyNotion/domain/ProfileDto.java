package in.studyNotion.domain;

import in.studyNotion.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
public class ProfileDto {

    @Id
    private ObjectId id;

    private Gender gender;

    private String about;

    private String dateOfBirth;
}
