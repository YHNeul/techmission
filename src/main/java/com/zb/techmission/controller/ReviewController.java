package com.zb.techmission.controller;

import com.zb.techmission.entity.Review;
import com.zb.techmission.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 작성
    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    // 특정 매장의 리뷰 조회
    @GetMapping("/{storeId}")
    public List<Review> getStoreReviews(@PathVariable Long storeId) {
        return reviewService.getReviewsByStoreId(storeId);
    }

    // 특정 리뷰 조회 (ID 기준)
    @GetMapping("/detail/{reviewId}")
    public Optional<Review> getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
