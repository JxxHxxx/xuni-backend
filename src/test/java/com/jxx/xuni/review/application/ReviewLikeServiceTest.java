package com.jxx.xuni.review.application;

import com.jxx.xuni.review.domain.ReviewLike;
import com.jxx.xuni.review.domain.ReviewLikeRepository;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@ServiceTest
class ReviewLikeServiceTest extends ServiceCommon {

    @Autowired
    ReviewLikeService reviewLikeService;
    @Autowired
    ReviewLikeRepository reviewLikeRepository;

    @BeforeEach
    void beforeEach() {
        reviewLikeRepository.deleteAll();
    }

    @DisplayName("리뷰 좋아요를 처음 누르면 좋아요 상태를 나타내는 isLike 필드 값이 True로 설정된다.")
    @Test
    void execute_first_time() {
        //given
        Long reviewId = 123123l;
        Long memberId = 1141l;
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isTrue();
    }

    @DisplayName("이미 좋아요를 누른 상태에서 좋아요를 실행할 경우 isLike 필드 값이 False로 변경된다.")
    @Test
    void execute_already_exist_and_liked_state_true() {
        //given
        Long reviewId = 123123l;
        Long memberId = 1141l;
        reviewLikeRepository.save(new ReviewLike(reviewId, memberId));
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isFalse();
    }

    @DisplayName("이미 좋아요를 취소한 상태에서 다시 좋아요를 누른 경우 다시 True로 변경된다.")
    @Test
    void execute_already_exist_and_liked_state_false() {
        //given
        Long reviewId = 123123l;
        Long memberId = 1141l;
        reviewLikeRepository.save(new ReviewLike(reviewId, memberId));
        reviewLikeService.execute(reviewId, memberId); // isLike 필드 -> false 로 변경
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isTrue();
    }
}