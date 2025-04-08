package com.mt365.sbootfds.controller;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageInfo;
import com.mt365.sbootfds.common.Result;
import com.mt365.sbootfds.entity.Admin;
import com.mt365.sbootfds.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;
    /*
     *添加管理员
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return Result.success();
    }
    /*
     *删除管理员
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    /*
     *批量删除管理员
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();
    }
    /*
     *更新管理员
     */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.updateById(admin);
        return Result.success();
    }
    /*
     *通过id查询管理员
     */
    @GetMapping("selectById/{id}")
    public Result findById(@PathVariable Integer id){
        Admin admin = adminService.findById(id);
        return Result.success(admin);
    }

    /*
     *查询所有管理员
     */
    @GetMapping("/selectAll")
    public Result findAll(String name){
        List<Admin> list = adminService.findAll(name);
        return Result.success(list);
    }

    /*
    分页查询所有管理员
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            String name,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo pageInfo = adminService.selectPage(pageNum,pageSize,name);
        return Result.success(pageInfo);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();  // 文件的原始名称
        // 写入文件
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:9999/files/download/" + fileName;
        return Result.success(url);
    }
//
//    /*
//     * 上传头像
//     */
//    @PostMapping("/uploadAvatar/{id}")
//    public Result uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
//        try {
//            String avatarPath = saveAvatar(file);
//            adminService.updateAvatar(id, avatarPath);
//            return Result.success();
//        } catch (IOException e) {
//            return Result.error("上传失败");
//        }
//    }
//    private String saveAvatar(MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        Path uploadPath = Paths.get("uploads");
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//        Path filePath = uploadPath.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        return "uploads/" + fileName; // 修改为相对路径
//    }

    /*
     * 下载头像
     */
    @GetMapping("/downloadAvatar/{id}")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Integer id) {
        String avatarPath = adminService.getAvatarPath(id);
        if (avatarPath == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Path path = Paths.get(avatarPath);
            byte[] imageBytes = Files.readAllBytes(path);
            String contentType = Files.probeContentType(path);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}

