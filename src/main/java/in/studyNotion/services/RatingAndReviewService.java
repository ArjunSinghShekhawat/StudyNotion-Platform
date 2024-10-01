package in.studyNotion.services;

import in.studyNotion.models.RatingAndReview;
import in.studyNotion.request.RatingAndReviewRequest;

import java.util.List;

public interface RatingAndReviewService {
    RatingAndReview createRatingAndReview(RatingAndReviewRequest ratingAndReviewRequest, String jwt) throws Exception;
    List<RatingAndReview>getAllRatingAndReview();
}
