package com.example.demo.src.file.Service;


import com.example.demo.src.file.Repository.FileRepository;
import com.example.demo.src.file.domain.Files;
import com.example.demo.src.file.vo.FileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


@AllArgsConstructor
@Service
public class FileService {




    private FileRepository fileRepository;


    //특정 파일 객체로 삭제
    public void deletePhotoFromFileSystem(List<Files> files) {
        try {
            for (Files file : files) {
                String photoPath = file.getFilepath();
                //String projectPath = System.getProperty("user.dir");
                File photoFile = new File("/src/main/resources/static/" + photoPath);



                // 파일이 존재하는지 확인하고 삭제
                if (photoFile.exists()) {
                    if (photoFile.delete()) {
                        System.out.println("사진 파일 삭제 성공: " + photoPath);
                    } else {
                        System.err.println("사진 파일 삭제 실패: " + photoPath);
                    }
                } else {
                    System.err.println("해당 경로에 사진 파일이 존재하지 않습니다: " + photoPath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("사진 파일 삭제 중 오류 발생: ");
        }
    }


    //fileId로 파일 삭제
    public void deleteFileSystem(List<Long> fileIdList) {
        try {
            for(Long fileId: fileIdList){
                Files file=fileRepository.findById(fileId).get();
                String photoPath = file.getFilepath();
              //  String projectPath = System.getProperty("user.dir");
                File photoFile = new File( "/src/main/resources/static/" + photoPath);

                fileRepository.deleteById(fileId);

                // 파일이 존재하는지 확인하고 삭제
                if (photoFile.exists()) {
                    if (photoFile.delete()) {
                        System.out.println("사진 파일 삭제 성공: " + photoPath);
                    } else {
                        System.err.println("사진 파일 삭제 실패: " + photoPath);
                    }
                } else {
                    System.err.println("해당 경로에 사진 파일이 존재하지 않습니다: " + photoPath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("사진 파일 삭제 중 오류 발생: ");
        }
    }



}
