package in.studyNotion.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class ContactUs {

    @Id
    private ObjectId id;

    private  String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String message;

    private String phoneNumber;

    private String countryCode;
}
