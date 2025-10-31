package com.example.kafka.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(
        name = "daily_views",
        uniqueConstraints = @UniqueConstraint(columnNames = {"recruit_id", "view_date"})
)
@AllArgsConstructor
public class JobRecruitDailyViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recruit_id", nullable = false)
    private Long recruitId;

    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    @Column(nullable = false)
    private Long views;

    // 기본 생성자
    public JobRecruitDailyViews() {}

    public JobRecruitDailyViews(Long recruitId, LocalDate viewDate, Long views) {
        this.recruitId = recruitId;
        this.viewDate = viewDate;
        this.views = views;
    }
}