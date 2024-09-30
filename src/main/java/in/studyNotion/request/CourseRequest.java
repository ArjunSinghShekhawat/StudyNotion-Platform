package in.studyNotion.request;

import in.studyNotion.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class CourseRequest {

    private String courseName;

    private String courseDescription;

    private String whatYouWillLearn;

    private long price;

    private Set<String>tags = new LinkedHashSet<>();

    private Set<String>instructions = new LinkedHashSet<>();

    private Status status;
}
