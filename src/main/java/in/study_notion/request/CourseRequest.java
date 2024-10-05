package in.study_notion.request;

import in.study_notion.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class CourseRequest {

    @NotBlank(message = "course name is required")
    private String courseName;

    @NotBlank(message = "course description is required")
    private String courseDescription;

    @NotBlank(message = "what you will learn is required")
    private String whatYouWillLearn;

    @NotNull
    private Long price;

    private Set<String>tags = new LinkedHashSet<>();

    private Set<String>instructions = new LinkedHashSet<>();

    private Status status;
}
