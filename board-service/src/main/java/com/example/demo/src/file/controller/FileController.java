//코드 수정할거 request,response DTO로 변환


package com.example.demo.src.file.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.file.Repository.FileRepository;
import com.example.demo.src.file.Service.FileService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.domain.Files;
import com.example.demo.src.file.vo.FileResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class FileController {

    private final FileService fileService;
    private FileRepository fileRepository;






 /*   //파일 객체로 삭제
    @DeleteMapping("/files/Object/delete")
    public  ResponseEntity<Response<String>> deleteFilesByObject(@RequestBody List<Files> files) throws BaseException{
        try {
            fileService.deletePhotoFromFileSystem(files); // 서비스 메서드 호출
            return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, "파일 삭제 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }*/


    //파일 다운로드
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId, HttpServletRequest request) throws IOException {

        Files files = fileRepository.findById(fileId).get();

        Resource resource = new FileUrlResource("src/main/resources/static"+files.getFilepath());
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/files/delete")
    public ResponseEntity<Response<String>> deleteFiles(@RequestBody List<Long> fileIdList) throws BaseException{
        try {
            fileService.deleteFileSystem(fileIdList); // 서비스 메서드 호출
            return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, "파일 삭제 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }
    }


}


