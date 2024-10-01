package in.studyNotion.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class SubSectionRequest {

    private String subSectionName;
    private String subSectionDescription;
    private ObjectId sectionId;
}
