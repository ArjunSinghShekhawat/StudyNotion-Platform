package in.studyNotion.repositories;

import in.studyNotion.models.RatingAndReview;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingAndReviewRepository extends MongoRepository<RatingAndReview, ObjectId> {
}
