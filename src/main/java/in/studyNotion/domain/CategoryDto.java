package in.studyNotion.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
public class CategoryDto {

    @Id
    private ObjectId id;

    @NotBlank(message = "name must be required !")
    @Indexed(unique = true)
    private String name;

    @NotBlank(message = "description must be required !")
    private String description;
}
