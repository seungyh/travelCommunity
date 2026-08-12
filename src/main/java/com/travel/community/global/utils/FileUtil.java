package com.travel.community.global.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

    /**
     * 폴더 생성
     * 
     * @param path
     * @return
     */
    public static File createDirectory(String path) {
        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs(); // 필요한 모든 상위 폴더와 함께 생성
            log.info("path created : {}", path);
        }
        return folder;
    }

    /**
     * multipart 파일 저장
     * 
     * @param path
     * @param file
     */
    public static void saveMultipartFile(String path, MultipartFile file) {

        File folder = createDirectory(path);
        try {
            file.transferTo(folder);
        } catch (IllegalStateException e) {
            log.error("Multipart File Save Error ", e);
        } catch (IOException e) {
            log.error("Multipart File Save Error ", e);
        } //

    }
}
