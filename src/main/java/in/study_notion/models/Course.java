package in.study_notion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.study_notion.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document
@Data
@NoArgsConstructor
public class Course {

    @Id
    private ObjectId id;

    private String courseName;

    private String courseDescription;

    @DBRef
    @JsonBackReference
    private User instructor;

    private String whatYouWillLearn;

    @DBRef
    private Set<Section> courseContent = new LinkedHashSet<>();

    @DBRef
    @JsonBackReference
    private List<RatingAndReview> ratingAndReviews = new ArrayList<>();

    private Long price;

    private String thumbnail;

    private Set<String> tags = new LinkedHashSet<>();

    @DBRef
    @JsonManagedReference
    private Category category;

    @DBRef
    private List<User> studentsEnrolled = new LinkedList<>();

    private Set<String> instructions = new LinkedHashSet<>();

    private LocalDateTime createdAt;

    private Status status;

    @Override
    public int hashCode() {
        return Objects.hash(id); // Only include the unique identifier
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references are the same
        if (!(obj instanceof Course)) return false; // Check if obj is of type Course
        Course other = (Course) obj;
        return Objects.equals(id, other.id); // Compare based on the unique identifier
    }
}
