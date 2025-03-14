package com.zb.techmission.controller;

import com.zb.techmission.entity.Review;
import com.zb.techmission.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 특정 매장의 리뷰 작성 폼
    @GetMapping("/create")
    public String showReviewForm(@RequestParam Long storeId,
                                 @RequestParam String username,
                                 @RequestParam String role,
                                 Model model) {
        // 리뷰 작성 가능 여부 확인
        boolean canWriteReview = reviewService.canWriteReview(storeId, username);

        if (!canWriteReview) {
            return "redirect:/stores/" + storeId + "?username=" + username + "&role=" + role + "&error=리뷰를 작성하려면 먼저 매장을 방문해야 합니다.";
        }

        model.addAttribute("storeId", storeId);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "review_form";
    }

    // 리뷰 작성 처리
    @PostMapping("/create")
    public String addReview(@RequestParam Long storeId,
                            @RequestParam String username,
                            @RequestParam String role,
                            @RequestParam String content,
                            RedirectAttributes redirectAttributes) {
        try {
            reviewService.addReview(storeId, username, content);
            redirectAttributes.addFlashAttribute("message", "리뷰가 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/stores/" + storeId + "?username=" + username + "&role=" + role;
    }

    // 내가 작성한 리뷰 목록
    @GetMapping("/my")
    public String myReviews(@RequestParam String username,
                            @RequestParam String role,
                            Model model) {
        List<Review> reviews = reviewService.getReviewsByUsername(username);

        model.addAttribute("reviews", reviews);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "my_reviews";
    }

    // 리뷰 수정 폼
    @GetMapping("/edit/{reviewId}")
    public String showEditForm(@PathVariable Long reviewId,
                               @RequestParam String username,
                               @RequestParam String role,
                               Model model) {
        Review review = reviewService.getReviewById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewId));

        // 작성자만 수정 가능
        if (!review.getUser().getUsername().equals(username)) {
            return "redirect:/reviews/my?username=" + username + "&role=" + role + "&error=자신이 작성한 리뷰만 수정할 수 있습니다.";
        }

        model.addAttribute("review", review);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "review_edit";
    }

    // 리뷰 수정 처리
    @PostMapping("/edit/{reviewId}")
    public String updateReview(@PathVariable Long reviewId,
                               @RequestParam String username,
                               @RequestParam String role,
                               @RequestParam String content,
                               RedirectAttributes redirectAttributes) {
        try {
            reviewService.updateReview(reviewId, username, content);
            redirectAttributes.addFlashAttribute("message", "리뷰가 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reviews/my?username=" + username + "&role=" + role;
    }

    // 리뷰 삭제
    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId,
                               @RequestParam String username,
                               @RequestParam String role,
                               @RequestParam(required = false) Long storeId,
                               RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(reviewId, username);
            redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // 매장 페이지에서 삭제한 경우 해당 매장으로 리다이렉트
        if (storeId != null) {
            return "redirect:/stores/" + storeId + "?username=" + username + "&role=" + role;
        }

        // 내 리뷰 목록에서 삭제한 경우 리뷰 목록으로 리다이렉트
        return "redirect:/reviews/my?username=" + username + "&role=" + role;
    }
}