package in.studyNotion.models;

import in.studyNotion.enums.AccountTypes;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Document
@Data
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private AccountTypes accountTypes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String password;

    private String phoneNumber;

    @DBRef
    private Profile profile;

    private String token;

    private LocalTime resetPasswordExpire;

    private String imageUrl;

}
