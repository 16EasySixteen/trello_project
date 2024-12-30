package com.example.trelloproject.card.service;

import com.example.trelloproject.card.dto.addFile.AddFileResponseDto;
import com.example.trelloproject.card.entity.AddFile;
import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.card.repository.AddFileRepository;
import com.example.trelloproject.card.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddFileService{

    private final AddFileRepository addFileRepository;
    private final CardRepository cardRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public AddFileService(AddFileRepository addFileRepository, CardRepository cardRepository, S3Client s3Client) {
        this.addFileRepository = addFileRepository;
        this.cardRepository = cardRepository;
        this.s3Client = s3Client;
    }
    /**
     * 파일 업로드 메서드
     */
    @Transactional
    public AddFileResponseDto uploadFiles(Long cardId, MultipartFile multipartFile) throws IOException {
        //카드 조회
        Card findCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다."));

        //고유 파일 이름 생성
        String fileName = generateUniqueFileName(multipartFile.getOriginalFilename());

        //S3에 파일 업로드
        uploadToS3(multipartFile, fileName);

        //S3 URL 생성
        String fileUrl = generateFileUrl(fileName);

        //생성자 주입
        AddFile addFile = new AddFile(multipartFile.getOriginalFilename(), fileName, fileUrl, multipartFile.getContentType(), multipartFile.getSize(), findCard);

        log.info(fileUrl);

        addFileRepository.save(addFile);

        return new AddFileResponseDto(
                addFile.getId(),
                addFile.getFilePath(),
                addFile.getOriginalFileName(),
                addFile.getFileType(),
                addFile.getCreatedAt(),
                addFile.getUpdatedAt()
        );
    }

    /**
     * S3에 파일 업로드
     */
    private void uploadToS3(MultipartFile multipartFile, String fileName) throws IOException {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
        } catch (Exception e) {
            throw new IOException("파일 업로드에 실패했습니다.", e);
        }
    }

    /**
     * S3 파일 URL 생성
     */
    private String generateFileUrl(String fileName) {
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toString();
    }

    /**
     * 고유한 파일 이름 생성
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "image/" + UUID.randomUUID().toString() + extension;
    }

    @Transactional
    public List<AddFileResponseDto> findFiles(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다. ID: " + cardId));
        List<AddFile> files = addFileRepository.findAllByCardId(cardId);
        return files.stream()
                .map(file -> new AddFileResponseDto(
                        file.getId(),
                        file.getFilePath(),
                        file.getOriginalFileName(),
                        file.getFileType(),
                        file.getCreatedAt(),
                        file.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteFile(Long fileId) {
        AddFile file = addFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다. ID: " + fileId));

        // S3에서 파일 삭제
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(file.getFilePath())
                .build());

        // DB에서 파일 삭제
        addFileRepository.delete(file);

    }
}
