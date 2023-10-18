package com.hanium.memotion.controller.global;

import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.BaseResponse;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.service.global.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AWSS3Controller {
    private final AWSS3Service awsS3Service;

    // S3 서버에 이미지 업로드
    @PostMapping("/s3")
    public BaseResponse<String> uploadFile(@RequestPart MultipartFile multipartFile) throws IOException {
        String fileName = null;

        if(multipartFile != null && !multipartFile.isEmpty()) {
            fileName = awsS3Service.uploadFile(multipartFile);
        }
        return BaseResponse.onSuccess(fileName);
    }

    // S3 서버에 저장된 이미지 교체
    @PatchMapping("/s3")
    public BaseResponse<String> modifyFile(@RequestParam("fileUrl") String fileUrl, @RequestPart MultipartFile multipartFile) throws IOException {
        if(fileUrl != null) {
            String[] url = fileUrl.split("/");
            boolean result = awsS3Service.deleteImage(url[3]);   // https~ 경로 뺴고 파일명으로 삭제

            if(result && multipartFile != null && !multipartFile.isEmpty()){
                String fileName = awsS3Service.uploadFile(multipartFile);
                return BaseResponse.onSuccess(fileName);
            }
        }

        throw new BaseException(ErrorCode.AWS_S3_ERROR);
    }
}
