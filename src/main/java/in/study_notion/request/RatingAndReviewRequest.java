package in.study_notion.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


@Data
@NoArgsConstructor
public class RatingAndReviewRequest {

    private String review;

    private ObjectId courseId;

    private byte  rating;
}
