package com.etm.modules.file.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {

    Map<String, Object> uploadFile(MultipartFile file, String type);

    void downloadFile(String type, String year, String month, String filename, HttpServletResponse response);

    void previewFile(String type, String year, String month, String filename, HttpServletResponse response);
}
