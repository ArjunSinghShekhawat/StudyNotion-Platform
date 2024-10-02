package in.study_notion.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor
public class OTP {

    @Id
    private ObjectId id;

    private String email;

    private String otp;

    private LocalDateTime createdAt;

    // Marked as transient, won't be stored in the database
    private static final long EXPIRATION_TIME_IN_MINUTES = 5;

}
