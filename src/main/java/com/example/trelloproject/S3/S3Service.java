package com.example.trelloproject.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {

        String fileName = generateUniqueFileName(multipartFile.getOriginalFilename());

        // PutObjectRequest를 생성하여 S3에 파일을 업로드
        putObjectToS3(multipartFile, fileName);

        // 업로드된 파일의 URL을 반환
        return generateFileUrl(fileName);
    }

    /**
     * S3에 파일을 업로드하는 메소드.
     */
    private void putObjectToS3(MultipartFile multipartFile, String fileName) throws IOException {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
    }

    /**
     * S3에서 업로드된 파일의 URL을 생성하여 반환하는 메소드.
     */
    private String generateFileUrl(String fileName) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        return s3Client.utilities().getUrl(request).toString();
    }

    /**
     * 고유한 파일 이름을 생성하는 메소드.
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "image/" + UUID.randomUUID().toString() + extension;
    }

    public void delete(String filePath) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}