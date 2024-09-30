package in.studyNotion.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class RatingAndReview {

    @Id
    private ObjectId id;

    @DBRef
    private User user;

    private String review;

    private byte  rating;

    @DBRef
    @JsonIgnore
    private Course course;
}
