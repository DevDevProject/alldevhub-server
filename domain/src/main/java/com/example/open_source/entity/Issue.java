package com.example.open_source.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "repository_issue")
@AllArgsConstructor
public class Issue extends TimeStamp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String state;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "html_url")
    private String htmlUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
