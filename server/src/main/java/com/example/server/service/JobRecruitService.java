package com.example.server.service;

import com.example.infra.s3.FileUploadService;
import com.example.server.dto.recruit.request.JobRecruitRequestDto;
import com.example.server.dto.recruit.request.JobSearchCondition;
import com.example.server.dto.recruit.response.JobRecruitListResponseDto;
import com.example.server.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.server.mapper.JobRecruitMapper;
import com.example.server.vo.JobRecruit;
import com.example.server.vo.jobrecruit.JobRecruitPaging;
import com.example.common.dto.kafka.RecruitCreatedMessage;
import com.example.common.dto.kafka.RecruitCountMessage;
import com.example.kafka.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRecruitService {

    private final JobRecruitMapper jobRecruitMapper;

    private final TypeService typeService;
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final DepartmentService departmentService;
    private final JobRecruitDetailService jobRecruitDetailService;
    private final KafkaProducerService kafkaProducerService;
    private final FileUploadService fileUploadService;

    @Transactional
    public void save(JobRecruitRequestDto[] requests) {
        try{
            for(JobRecruitRequestDto request : requests) {
                Long departmentId = departmentService.getDepartmentId(request.getBasic().getDepartment());
                Long companyId = companyService.getCompanyId(request.getBasic().getCompany());
                Long typeId = typeService.getTypeId(request.getBasic().getType(), request.getBasic().getTitle());

                JobRecruit jobRecruit = new JobRecruit(request.getBasic().getTitle(), request.getBasic().getWorkExperience(), request.getBasic().getUrl(),
                        departmentId, companyId, typeId, request.getBasic().getDeadline(), request.getBasic().getPostingType());

                jobRecruitMapper.insert(jobRecruit);

                jobRecruitDetailService.save(request, jobRecruit.getId());
                categoryService.save(request.getBasic().getCategory(), request.getDetail(), jobRecruit.getId());

                RecruitCountMessage countMessage = new RecruitCountMessage(request.getBasic().getCompany(), "count 증가");

                RecruitCreatedMessage createMessage = new RecruitCreatedMessage(jobRecruit.getId(), companyId,
                        request.getBasic().getCompany(), request.getBasic().getTitle(),
                        LocalDateTime.now(), "recruit 생성");

                kafkaProducerService.sendRecruitCountEvent(countMessage);
                kafkaProducerService.sendRecruitCreatedEvent(createMessage);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void saveOne(JobRecruitRequestDto request, MultipartFile image) throws IOException {
        try {
            String imageUrl = fileUploadService.uploadImage(image);

            Long departmentId = departmentService.getDepartmentId(request.getBasic().getDepartment());
            Long companyId = companyService.getCompanyId(request.getBasic().getCompany());
            Long typeId = typeService.getTypeId(request.getBasic().getType(), request.getBasic().getTitle());

            JobRecruit jobRecruit = new JobRecruit(request.getBasic().getTitle(), request.getBasic().getWorkExperience(), request.getBasic().getUrl(),
                    departmentId, companyId, typeId, request.getBasic().getDeadline(), request.getBasic().getPostingType());

            jobRecruitMapper.insert(jobRecruit);

            jobRecruitDetailService.save(imageUrl, jobRecruit.getId());
            categoryService.save(request.getBasic().getCategory(), request.getDetail().getBody(), jobRecruit.getId());

            RecruitCountMessage countMessage = new RecruitCountMessage(request.getBasic().getCompany(), "count 증가");

            RecruitCreatedMessage createMessage = new RecruitCreatedMessage(jobRecruit.getId(), companyId,
                    request.getBasic().getCompany(), request.getBasic().getTitle(),
                    LocalDateTime.now(), "recruit 생성");

            kafkaProducerService.sendRecruitCountEvent(countMessage);
            kafkaProducerService.sendRecruitCreatedEvent(createMessage);

        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public JobRecruitListResponseDto search(JobSearchCondition condition, Pageable pageable, String sort) {
        List<JobRecruitPaging> searchedRecruits = jobRecruitMapper.findAllWithConditions(condition, pageable, sort);
        Integer count = jobRecruitMapper.findAllWithConditionsCount(condition);

        return new JobRecruitListResponseDto(searchedRecruits, count);
    }


    public List<String> getAllUrls() {
        return jobRecruitMapper.findAllUrls();
    }

    public RecruitDetailDataDto getRecruitDetail(Long recruitId) {
        return jobRecruitDetailService.getDetails(recruitId);
    }

    public JobRecruitListResponseDto getRecruitsByCompany(String companyName, Pageable pageable) {
        List<JobRecruitPaging> recruits = jobRecruitMapper.findRecruitsByCompany(companyName, pageable);
        Integer count = jobRecruitMapper.findRecruitsByCompanyCount(companyName);

        return new JobRecruitListResponseDto(recruits, count);
    }
}
