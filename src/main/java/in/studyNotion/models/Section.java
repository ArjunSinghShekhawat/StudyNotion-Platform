package in.studyNotion.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Document
public class Section {

    @Id
    private ObjectId id;

    private String sectionName;

    @DBRef
    private Set<SubSection>subSections = new LinkedHashSet<>();
}
