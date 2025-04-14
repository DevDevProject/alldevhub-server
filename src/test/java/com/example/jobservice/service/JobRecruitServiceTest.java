//package com.example.jobservice.service;
//
//import com.example.jobservice.vo.JobRecruit;
//import com.example.jobservice.mapper.JobRecruitMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class JobRecruitServiceTest {
//
//    @Mock
//    private JobRecruitMapper jobRecruitMapper;
//
//    @InjectMocks
//    private JobRecruitService jobRecruitService;
//
//    @Test
//    void getJobsByCompany() {
//        Long companyId = 1L;
//        List<JobRecruit> mockJobs = Arrays.asList(
//                new JobRecruit(1L, "Backend Developer 모집", "1년", "https://test.com", companyId, 1L, 1L, 1L),
//                new JobRecruit(2L, "Frontend Developer 모집", "2년", "https://example.com", companyId, 1L, 1L, 1L)
//        );
//
//        when(jobRecruitMapper.findByCompanyId(companyId)).thenReturn(mockJobs);
//
//        List<JobRecruit> result = jobRecruitService.getJobsByCompany(companyId);
//
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getTitle()).isEqualTo("Backend Developer 모집");
//        assertThat(result.get(1).getTitle()).isEqualTo("Frontend Developer 모집");
//
//        verify(jobRecruitMapper, times(1)).findByCompanyId(companyId);
//    }
//}