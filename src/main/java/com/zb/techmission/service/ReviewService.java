package com.zb.techmission.service;

import com.zb.techmission.entity.Review;
import com.zb.techmission.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 저장
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    // 특정 매장의 리뷰 조회
    public List<Review> getReviewsByStoreId(Long storeId) {
        return reviewRepository.findByStoreId(storeId);
    }

    // 특정 리뷰 조회 (ID 기준)
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    // 특정 리뷰 삭제
    public String deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return "Review deleted successfully";
        } else {
            return "Review not found";
        }
    }
}
