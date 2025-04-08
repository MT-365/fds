package com.mt365.sbootfds.controller;

import cn.hutool.core.io.FileUtil;
import com.mt365.sbootfds.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 处理文件上传和下载的控制器
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Value("${file.download.base.url}")
    private String fileDownloadBaseUrl;

    @Value("${file.max.size}")
    private long MAX_FILE_SIZE;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 文件上传
     * http://localhost:9999/files/upload
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result upload(@RequestParam("file") MultipartFile file, String userName) {
        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            //当前根目录+文件上传的目录
//            String filePath = fileUploadPath + "/files/";
            String filePath = System.getProperty("user.dir") + "/files/";
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }

            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                return Result.error("文件大小超过限制");
            }

            byte[] bytes = file.getBytes();
//          String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();  // 文件的原始名称
            String fileName = userName + "-" + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date()) + ".png";

// 检查并删除前缀为 userName 的文件
            File directory = new File(filePath);
            System.out.println("文件目录: " + directory.getAbsolutePath());  // 打印文件的绝对路径
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles((dir, name) -> name.startsWith(userName));
                if (files != null) {
                    for (File fileIndex : files) {
                        if (fileIndex.delete()) {
                            // 文件删除成功
                            System.out.println("文件 " + fileIndex.getName() + " 已删除");
                        } else {
                            // 文件删除失败
                            System.out.println("文件 " + fileIndex.getName() + " 删除失败");
                        }
                    }
                }
            }
            FileUtil.writeBytes(bytes, filePath + fileName);
            // 使用日志框架记录日志
            logger.info("文件上传成功: {}", fileName);

            // 返回文件的访问路径
            String url = fileDownloadBaseUrl + "files/download/" + fileName;
            return Result.success(url);
        } catch (IOException e) {
            logger.error("文件上传失败: {}", e.getMessage());
            return Result.error("文件上传失败");
        }
    }

    /**
     * 文件下载
     * 下载地址：http://localhost:9999/files/download/123.jpg
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/"; // 获取当前项目的根路径
        String realPath = filePath + fileName;  // 文件的真实路径
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new RuntimeException("文件不存在");
        }
        // 读取文件的字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream outputStream = response.getOutputStream();
        // 输出流对象把文件写出到客户端
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
