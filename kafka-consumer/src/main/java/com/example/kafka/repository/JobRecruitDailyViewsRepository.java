package com.example.kafka.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.example.kafka.entity.JobRecruitDailyViews;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRecruitDailyViewsRepository extends JpaRepository<JobRecruitDailyViews, Long> {

    Optional<JobRecruitDailyViews> findByRecruitIdAndViewDate(Long recruitId, LocalDate viewDate);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO job_recruit_daily_views (recruit_id, view_date, views) " +
                    "VALUES (:recruitId, :viewDate, :views) " +
                    "ON DUPLICATE KEY UPDATE views = views + VALUES(views)",
            nativeQuery = true
    )
    void batchInsert(@Param("recruitId") Long recruitId,
                @Param("viewDate") LocalDate viewDate,
                @Param("views") Long views);
}
