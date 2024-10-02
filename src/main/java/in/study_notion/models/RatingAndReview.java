package in.study_notion.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class RatingAndReview {

    @Id
    private ObjectId id;

    @DBRef
    @Indexed(unique = true)
    private User user;

    private String review;

    private byte  rating;

    @DBRef
    @JsonManagedReference
    private Course course;
}
