package in.study_notion.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class SubSection {

    @Id
    private ObjectId id;

    private String subSectionName;

    private String subSectionDescription;

    private String timeDuration;

    private String videoUrl;
}
