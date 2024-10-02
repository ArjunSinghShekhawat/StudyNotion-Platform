package in.study_notion.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Objects;
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
    @JsonBackReference
    private Set<Course> courses = new LinkedHashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, name); // Choose unique identifiers
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        Category other = (Category) obj;
        return Objects.equals(id, other.id);
    }
}
