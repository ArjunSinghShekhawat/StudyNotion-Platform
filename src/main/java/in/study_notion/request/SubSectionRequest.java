package in.study_notion.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class SubSectionRequest {

    @NotBlank(message = "sub-section name is required")
    private String subSectionName;

    @NotBlank(message = "sub section description is required")
    private String subSectionDescription;

    @NotNull(message = "section is required")
    private ObjectId sectionId;
}
