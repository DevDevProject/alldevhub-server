package com.example.common.dto.common;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JobRecruitDailyView {

    private Long recruitId;
    private LocalDate viewDate;
    private Long views;

    public static JobRecruitDailyView create(Long recruitId, LocalDate viewDate, Long views) {
        return  JobRecruitDailyView.builder()
                                   .recruitId(recruitId)
                                   .viewDate(viewDate)
                                   .views(views)
                                   .build();
    }
}
