package in.study_notion.repositories;

import in.study_notion.models.OTP;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends MongoRepository<OTP, ObjectId> {

    OTP findByEmail(String email);
    void deleteByEmail(String email);
    Optional<OTP> findFirstByEmailOrderByCreatedAtDesc(String email);
}
