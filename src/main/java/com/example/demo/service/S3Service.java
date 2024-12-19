package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3Client;
    private final AmazonS3 amazonS3;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    @Transactional
    public String uploadImage(MultipartFile image) throws IOException {
        // 이미지 파일
        if(image != null) {
            // 형식
            String extension = getImageExtension(image);

            // 파일 이름 생성
            String filName = UUID.randomUUID().toString() + "_profile" + extension;

            // 메타데이터
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            // 업로드
            PutObjectRequest request = null;
            try{
                request = new PutObjectRequest(bucketName, filName, image.getInputStream(), metadata);
            }catch (IOException e){
                throw new IOException(e.getMessage());
            }

            // 업로드
            amazonS3.putObject(request);

            return getPublicUrl(filName);
        }

        return "No Photo";
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, amazonS3.getRegionName() ,fileName);
    }

    private String getImageExtension(MultipartFile image) {
        String extension = "";
        String originalFilename = image.getOriginalFilename();

        if(originalFilename!=null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 검증
        if(!extension.equals(".png") && !extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".gif")) {
            throw new InvalidDataAccessApiUsageException("잘못된 형식입니다.");
        }
        return extension;
    }
}
