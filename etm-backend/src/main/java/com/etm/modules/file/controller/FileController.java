package com.etm.modules.file.controller;

import com.etm.common.result.Result;
import com.etm.modules.file.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public Result<Map<String, Object>> uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "other") String type) {
        return Result.ok(fileService.uploadFile(file, type));
    }

    @GetMapping("/download/{type}/{year}/{month}/{filename}")
    public void downloadFile(
            @PathVariable String type,
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String filename,
            HttpServletResponse response) {
        fileService.downloadFile(type, year, month, filename, response);
    }

    @GetMapping("/preview/{type}/{year}/{month}/{filename}")
    public void previewFile(
            @PathVariable String type,
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String filename,
            HttpServletResponse response) {
        fileService.previewFile(type, year, month, filename, response);
    }
}
