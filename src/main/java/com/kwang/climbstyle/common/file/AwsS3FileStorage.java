package com.kwang.climbstyle.common.file;

import com.kwang.climbstyle.code.file.FileErrorCode;
import com.kwang.climbstyle.exception.ClimbStyleException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class AwsS3FileStorage implements FileStorage {

    private final S3Client s3Client;

    @Value("${file.s3.bucket}")
    private String bucket;

    @Value("${file.s3.base-url}")
    private String baseUrl;

    @Override
    public String upload(String subPath, String storedFilename, MultipartFile file) throws IOException {
        String s3Key = subPath + storedFilename;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new ClimbStyleException(FileErrorCode.FILE_UPLOAD_ERROR);
        }

        return baseUrl + s3Key;
    }

    @Override
    public void delete(String fileUrl) {
        String s3Key = fileUrl.replace(baseUrl, "");

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        try {
            s3Client.deleteObject(request);
        } catch (S3Exception e) {
            throw new ClimbStyleException(FileErrorCode.FILE_DELETE_ERROR);
        }
    }
}
