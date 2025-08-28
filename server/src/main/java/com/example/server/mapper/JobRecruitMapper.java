package com.example.server.mapper;
import com.example.server.dto.recruit.request.JobSearchCondition;
import com.example.server.dto.recruit.response.RecruitDetailResponseDto;
import com.example.server.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.server.vo.JobRecruit;
import com.example.server.vo.jobrecruit.JobRecruitPaging;
import com.example.server.vo.JobRecruit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobRecruitMapper {

    void insert(JobRecruit jobRecruit);

    List<JobRecruitPaging> findAll(@Param("pageable") Pageable pageable);

    Integer findAllCount();

//    List<JobRecruitPaging> findAllWithConditions(
////            @Param("condition") JobSearchCondition condition,
//            @Param("categories") List<String> categories,
//            @Param("stacks") List<String> stacks,
//            @Param("workExperiences") List<String> workExperiences,
//            @Param("offset") long offset,
//            @Param("pageSize") int pageSize
//    );

    List<JobRecruitPaging> findAllWithConditions(
            @Param("condition") JobSearchCondition condition,
            @Param("pageable") Pageable pageable,
            @Param("sort") String sort
    );


    Integer findAllWithConditionsCount(@Param("condition") JobSearchCondition condition);

    List<String> findAllUrls();

    List<JobRecruitPaging> findRecruitsByCompany(@Param("companyName") String companyName, @Param("pageable") Pageable pageable);

    Integer findRecruitsByCompanyCount(@Param("companyName") String companyName);

}
