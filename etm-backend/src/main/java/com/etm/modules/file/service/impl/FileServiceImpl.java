package com.etm.modules.file.service.impl;

import com.etm.common.exception.BusinessException;
import com.etm.modules.file.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-prefix}")
    private String accessPrefix;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf", "docx", "xlsx");

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        String extension = getFileExtension(originalName);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }

        String fileName = UUID.randomUUID().toString() + "." + extension;
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));

        Path dirPath = Paths.get(uploadPath, type, year, month);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new BusinessException("创建目录失败: " + e.getMessage());
        }

        Path filePath = dirPath.resolve(fileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new BusinessException("文件保存失败: " + e.getMessage());
        }

        String fileUrl = accessPrefix + "/" + type + "/" + year + "/" + month + "/" + fileName;

        Map<String, Object> result = new HashMap<>();
        result.put("fileName", originalName);
        result.put("fileUrl", fileUrl);
        result.put("fileSize", file.getSize());

        return result;
    }

    @Override
    public void downloadFile(String type, String year, String month, String filename, HttpServletResponse response) {
        Path filePath = Paths.get(uploadPath, type, year, month, filename);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }

        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentLength((int) file.length());

            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            throw new BusinessException("文件下载失败: " + e.getMessage());
        }
    }

    @Override
    public void previewFile(String type, String year, String month, String filename, HttpServletResponse response) {
        Path filePath = Paths.get(uploadPath, type, year, month, filename);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }

        try {
            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
            response.setContentLength((int) file.length());

            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            throw new BusinessException("文件预览失败: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
