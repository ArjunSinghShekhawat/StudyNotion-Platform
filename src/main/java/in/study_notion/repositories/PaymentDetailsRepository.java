package in.study_notion.repositories;

import in.study_notion.models.PaymentDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends MongoRepository<PaymentDetails, ObjectId> {
    PaymentDetails findByUserId(ObjectId userId);
}
