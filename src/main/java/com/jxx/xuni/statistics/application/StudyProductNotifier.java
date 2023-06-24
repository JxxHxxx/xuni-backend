package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.event.trigger.StudyProductCreatedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewCreatedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewDeletedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.*;

//TODO : 비즈니스 로직 COMMIT / 이벤트 처리 ROLLBACK 되는 상황에서 어떻게 재시도할지 방법을 구상 및 구현

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StudyProductNotifier {

    private final StudyProductStatisticsService studyProductStatisticsService;

    @TransactionalEventListener(classes = StudyProductCreatedEvent.class, phase = AFTER_COMMIT)
    public void handle(StudyProductCreatedEvent event) {
        studyProductStatisticsService.create(event.studyProductId());
    }

    @TransactionalEventListener(value = ReviewCreatedEvent.class, phase = AFTER_COMMIT)
    public void handle(ReviewCreatedEvent event) {
        studyProductStatisticsService.reflectReviewCreate(event.rating(), event.studyProductId());
    }

    @TransactionalEventListener(value = ReviewUpdatedEvent.class, phase = AFTER_COMMIT)
    public void handle(ReviewUpdatedEvent event) {
        studyProductStatisticsService.reflectReviewUpdate(event.studyProductId(), event.ratingBeforeUpdate(), event.updatedRating());
    }

    @TransactionalEventListener(value = ReviewDeletedEvent.class, phase = AFTER_COMMIT)
    public void handle(ReviewDeletedEvent event) {
        studyProductStatisticsService.reflectReviewDelete(event.studyProductId(), event.rating());
    }
}