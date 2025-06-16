package com.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.UUID;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.prefix}")
    private String prefix;

    public String uploadImage(MultipartFile file) throws IOException {
        // 원래 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // UUID 생성 + 확장자 붙이기
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // 최종 저장 경로
        String key = prefix + "/" + uniqueFilename;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        String fileUrl = "https://cdn.alldevhub.com/" + key;
        System.out.println("File uploaded successfully at: " + fileUrl);

        return fileUrl;
    }
}
