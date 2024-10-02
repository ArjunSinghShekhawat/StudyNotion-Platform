package in.study_notion.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class SectionRequest {

    @NotBlank(message = "course id is required")
    private ObjectId courseId;

    @NotBlank(message = "section name is required")
    private String sectionName;
}
