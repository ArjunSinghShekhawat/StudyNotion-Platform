package in.studyNotion.domain;

import in.studyNotion.enums.Status;
import in.studyNotion.models.Category;
import in.studyNotion.models.RatingAndReview;
import in.studyNotion.models.Section;
import in.studyNotion.models.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
public class CourseDto {
    @Id
    private ObjectId id;

    private String courseName;

    private String courseDescription;

    @DBRef
    private ObjectId instructor;

    private String whatYouWillLearn;

    @DBRef
    private Set<Section> courseContent = new LinkedHashSet<>();

    @DBRef
    private List<RatingAndReview> ratingAndReviews = new ArrayList<>();

    private long price;

    private String thumbnail;

    private Set<String>tags = new LinkedHashSet<>();

    @DBRef
    private ObjectId category;

    @DBRef
    private List<User>studentsEnrolled = new LinkedList<>();

    private Set<String>instructions = new LinkedHashSet<>();

    private LocalDateTime createdAt;

    private Status status;
}
