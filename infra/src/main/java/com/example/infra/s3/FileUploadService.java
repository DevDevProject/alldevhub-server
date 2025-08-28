package com.example.infra.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.UUID;
import java.io.IOException;

@RequiredArgsConstructor
public class FileUploadService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String prefix;

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
