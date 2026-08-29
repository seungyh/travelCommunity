package com.travel.community.global.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

    private static final DateTimeFormatter FILE_PATH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

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

    /**
     * 파일 경로를 찾아서 Resource로 응답
     * 
     * @param filePath
     * @return
     */
    public static Resource getResource(String filePath) {
        Path path = Paths.get(filePath);
        return new FileSystemResource(path);
    }

    /**
     * 현재 날짜를 기준으로 파일 저장용 상대 경로를 생성한다.
     * 예: 2026/08/26
     *
     * @return yyyy/MM/dd 형식의 파일 저장 경로
     */
    public static String getDatePath() {
        return LocalDate.now().format(FILE_PATH_DATE_FORMATTER) + "/";
    }
}
