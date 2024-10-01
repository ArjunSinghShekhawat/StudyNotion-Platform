package in.studyNotion.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class SectionRequest {

    private ObjectId courseId;
    private String sectionName;
}
