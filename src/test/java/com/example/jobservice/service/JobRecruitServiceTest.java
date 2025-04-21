package com.example.jobservice.service;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.util.Classifier;
import com.example.jobservice.vo.JobRecruit;
import com.example.jobservice.mapper.JobRecruitMapper;
import com.example.jobservice.vo.Stack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobRecruitServiceTest {

    @Mock
    private JobRecruitMapper jobRecruitMapper;

    @InjectMocks
    private JobRecruitService jobRecruitService;

    @Mock
    private JobRecruitDetailService jobRecruitDetailService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private CompanyService companyService;

    @Mock
    private TypeService typeService;

    @Mock
    private Classifier classifier;

    @Test
    void getJobsByCompany() {
        Long companyId = 1L;
        List<JobRecruit> mockJobs = Arrays.asList(
                new JobRecruit(1L, "Backend Developer 모집", "1년", "https://test.com", companyId, 1L, 1L, 1L),
                new JobRecruit(2L, "Frontend Developer 모집", "2년", "https://example.com", companyId, 1L, 1L, 1L)
        );

        when(jobRecruitMapper.findByCompanyId(companyId)).thenReturn(mockJobs);

        List<JobRecruit> result = jobRecruitService.getJobsByCompany(companyId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Backend Developer 모집");
        assertThat(result.get(1).getTitle()).isEqualTo("Frontend Developer 모집");

        verify(jobRecruitMapper, times(1)).findByCompanyId(companyId);
    }

    @Test
    void saveTest() {
        JobRecruitRequestDto request = new JobRecruitRequestDto();
        request.setBasic(new JobRecruitRequestDto.BasicInfo());
        request.setDetail(new JobRecruitRequestDto.DetailInfo());

        request.getBasic().setTitle("개발자 채용");
        request.getBasic().setCompany("Google");
        request.getBasic().setDepartment("개발팀");
        request.getBasic().setType("정규직");
        request.getBasic().setCategory("Backend");

        JobRecruitRequestDto[] requests = { request };

        when(companyService.getCompanyId("Google")).thenReturn(1L);
        when(departmentService.getDepartmentId("개발팀")).thenReturn(2L);
        when(typeService.getTypeId("정규직")).thenReturn(3L);

        // stub
        doAnswer(invocation -> {
            JobRecruit job = invocation.getArgument(0);
            job.setId(100L);
            return null;
        }).when(jobRecruitMapper).insert(any(JobRecruit.class));

        Stack stack = new Stack();
        stack.setId(10L);
        stack.setName("Java");

        Classifier.ClassificationResult classificationResult =
                new Classifier.ClassificationResult(List.of(stack), List.of("Backend"));

        // when
        jobRecruitService.save(requests);

        // then
        verify(jobRecruitMapper, times(1)).insert(any(JobRecruit.class));
        verify(jobRecruitDetailService, times(1)).save(eq(request), eq(100L));
        verify(categoryService, times(1)).save(eq("Backend"), any(), eq(100L));
    }
}