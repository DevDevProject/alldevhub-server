package com.example.common.dto.kafka;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenSourceRepoCreateDto {

    @JsonProperty("repository_id")
    private Long repositoryId;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("html_url")
    private String htmlUrl;
    private String description;

    @JsonProperty("main_language")
    private String mainLanguage;

    @JsonProperty("forks_count")
    private Integer forksCount;

    @JsonProperty("stars_count")
    private Integer starsCount;

    @JsonProperty("issues_count")
    private Integer issuesCount;

    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createTime;

    @JsonProperty("update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updateTime;
}
