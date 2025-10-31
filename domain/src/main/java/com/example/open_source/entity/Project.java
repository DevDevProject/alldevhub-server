package com.example.open_source.entity;

import com.example.common.dto.kafka.OpenSourceRepoCreateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "repository")
public class Project extends TimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repository_id")
    private Long repositoryId;

    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "main_language")
    private String mainLanguage;
    private String description;

    @Column(name = "forks_count")
    private Integer forksCount;

    @Column(name = "stars_count")
    private Integer starsCount;

    @Column(name = "issues_count")
    private Integer issuesCount;

    @Column(name = "html_url")
    private String htmlUrl;

    public static Project make(OpenSourceRepoCreateDto dto) {
        Project project = new Project();
        project.setRepositoryId(dto.getRepositoryId());
        project.setName(dto.getName());
        project.setFullName(dto.getFullName());
        project.setMainLanguage(dto.getMainLanguage());
        project.setDescription(dto.getDescription());
        project.setForksCount(dto.getForksCount());
        project.setStarsCount(dto.getStarsCount());
        project.setIssuesCount(dto.getIssuesCount());
        project.setHtmlUrl(dto.getHtmlUrl());

        return project;
    }
}
