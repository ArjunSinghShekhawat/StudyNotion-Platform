package in.study_notion.services;

import in.study_notion.models.RatingAndReview;
import in.study_notion.request.RatingAndReviewRequest;

import java.util.List;

public interface RatingAndReviewService {
    RatingAndReview createRatingAndReview(RatingAndReviewRequest ratingAndReviewRequest, String jwt) throws Exception;
    List<RatingAndReview>getAllRatingAndReview();
}
