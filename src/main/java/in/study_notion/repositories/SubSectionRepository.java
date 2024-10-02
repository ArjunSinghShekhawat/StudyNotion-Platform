package in.study_notion.repositories;

import in.study_notion.models.SubSection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubSectionRepository extends MongoRepository<SubSection, ObjectId> {
}
