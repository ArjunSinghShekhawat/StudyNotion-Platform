package in.studyNotion.request;

import in.studyNotion.enums.Status;
import in.studyNotion.models.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.*;

@Data
@NoArgsConstructor
public class CourseRequest {

    private String courseName;

    private String courseDescription;

    private String whatYouWillLearn;

    private long price;

    private Set<String>tags = new LinkedHashSet<>();

    private ObjectId category;

    private Set<String>instructions = new LinkedHashSet<>();

    private Status status;
}
