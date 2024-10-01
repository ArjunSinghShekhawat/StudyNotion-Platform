package in.studyNotion.models;

import in.studyNotion.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@NoArgsConstructor
public class Profile {

    @Id
    private ObjectId id;

    private Gender gender;

    private String dateOfBirth;

    private String about;
}
