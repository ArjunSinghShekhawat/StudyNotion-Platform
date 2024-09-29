package in.studyNotion.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
public class Category {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String name;

    private String description;

    @DBRef
    private Set<Course> courses = new LinkedHashSet<>();
}
