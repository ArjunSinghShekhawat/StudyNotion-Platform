package in.study_notion.repositories;


import in.study_notion.models.ContactUs;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends MongoRepository<ContactUs, ObjectId> {
}
