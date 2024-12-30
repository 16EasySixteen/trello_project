package com.example.trelloproject.card.controller;

import com.example.trelloproject.S3.S3Service;
import com.example.trelloproject.card.dto.addFile.AddFileResponseDto;
import com.example.trelloproject.card.service.AddFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/cards/{cardId}/files")
public class AddFileController {


    private final AddFileService addFileService;
    private final S3Service s3Service;

    public AddFileController(AddFileService addfileService, S3Service s3Service) {
        this.addFileService = addfileService;
        this.s3Service = s3Service;
    }

    //파일 업로드
    @PostMapping
    public ResponseEntity<AddFileResponseDto> uploadFiles(
            @PathVariable long cardId,
            @RequestPart MultipartFile file) throws IOException {

        return new ResponseEntity<>(addFileService.uploadFiles(cardId ,file), HttpStatus.CREATED);
    }

    //파일 조회
    @GetMapping
    public ResponseEntity<List<AddFileResponseDto>> findFiles(
            @PathVariable Long cardId) {

        return new ResponseEntity<>(addFileService.findFiles(cardId), HttpStatus.OK);
    }

    //파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long fileId ) {

        addFileService.deleteFile(fileId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
