package com.zb.techmission.service;

import com.zb.techmission.entity.Reservation;
import com.zb.techmission.entity.Review;
import com.zb.techmission.entity.Store;
import com.zb.techmission.entity.StoreUser;
import com.zb.techmission.repository.ReservationRepository;
import com.zb.techmission.repository.ReviewRepository;
import com.zb.techmission.repository.StoreRepository;
import com.zb.techmission.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         StoreRepository storeRepository,
                         ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.reservationRepository = reservationRepository;
    }

    // 리뷰 작성이 가능한지 확인 (방문 확인된 예약이 있는지)
    public boolean canWriteReview(Long storeId, String username) {
        List<Reservation> confirmedReservations = reservationRepository.findByStoreIdAndUserUsernameAndConfirmedTrue(storeId, username);
        return !confirmedReservations.isEmpty();
    }

    // 리뷰 저장
    @Transactional
    public Review addReview(Long storeId, String username, String content) {
        // 매장과 사용자 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + storeId));

        StoreUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        // 리뷰 작성 가능 여부 확인
        if (!canWriteReview(storeId, username)) {
            throw new IllegalStateException("방문 확인된 예약이 없어 리뷰를 작성할 수 없습니다.");
        }

        // 리뷰 생성 및 저장
        Review review = new Review();
        review.setStore(store);
        review.setUser(user);
        review.setContent(content);

        return reviewRepository.save(review);
    }

    // 특정 매장의 리뷰 조회
    public List<Review> getReviewsByStoreId(Long storeId) {
        return reviewRepository.findByStoreId(storeId);
    }

    // 사용자가 작성한 리뷰 조회
    public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByUserUsername(username);
    }

    // 특정 리뷰 조회 (ID 기준)
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    // 리뷰 수정 (작성자만 가능)
    @Transactional
    public Review updateReview(Long reviewId, String username, String newContent) {
        // 리뷰 조회 및 작성자 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewId));

        if (!review.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("자신이 작성한 리뷰만 수정할 수 있습니다.");
        }

        review.setContent(newContent);
        return reviewRepository.save(review);
    }

    // 리뷰 삭제 (작성자 또는 매장 점주만 가능)
    @Transactional
    public void deleteReview(Long reviewId, String username) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewId));

        // 작성자 또는 매장 점주인지 확인
        boolean isAuthor = review.getUser().getUsername().equals(username);
        boolean isStoreOwner = review.getStore().getOwner().getUsername().equals(username);

        if (!isAuthor && !isStoreOwner) {
            throw new IllegalStateException("리뷰 작성자 또는 매장 점주만 리뷰를 삭제할 수 있습니다.");
        }

        reviewRepository.deleteById(reviewId);
    }
}