package com.mt365.sbootfds.service;


import cn.hutool.core.util.ObjectUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mt365.sbootfds.common.RoleEnum;
import com.mt365.sbootfds.entity.Account;
import com.mt365.sbootfds.entity.Admin;
import com.mt365.sbootfds.exception.CustomException;
import com.mt365.sbootfds.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Component
public class AdminService {
    @Resource
    AdminMapper adminMapper;

    public Admin login(Account account) {
        String username = account.getUsername();
        //根据账号查询用户信息
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {
            throw new CustomException("账号不存在！");
        }
        //校验密码
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomException("账号或者密码错误！");
        }
        return dbAdmin;
    }

    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new CustomException("账号已经存在！");
        }
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword("admin");
        }

        admin.setRole(RoleEnum.ADMIN.name());
        adminMapper.insert(admin);
    }

    /**
     * 根据ID删除管理员账号
     *
     * @param id 管理员ID
     */
    public void deleteById(Integer id) {
        // 获取管理员信息
        Admin admin = adminMapper.selectById(id);
        if (admin != null && admin.getAvatar() != null) {
            // 删除头像文件
            String filePath = System.getProperty("user.dir") + "/files/";
            File avatarFile = new File(filePath + admin.getAvatar().substring(admin.getAvatar().lastIndexOf("/") + 1));
            System.out.println("头像文件路径: " + avatarFile.getAbsolutePath());  // 打印文件的绝对路径
            if (avatarFile.exists() && avatarFile.isFile()) {
                if (avatarFile.delete()) {
                    // 头像文件删除成功
                    System.out.println("头像文件 " + admin.getAvatar() + " 已删除");
                } else {
                    // 头像文件删除失败
                    System.out.println("头像文件 " + admin.getAvatar() + " 删除失败");
                }
            } else {
                // 头像文件不存在
                System.out.println("头像文件 " + admin.getAvatar() + " 不存在");
            }
        }

        // 删除管理员账号
        adminMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    public Admin findById(Integer id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> findAll(String name) {
        return adminMapper.selectAll(name);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize); // 分页查询
        List<Admin> list = this.findAll(name); // 查询所有
        return PageInfo.of(list);  // 返回分页信息
    }

    public void updateAvatar(Integer id, String avatarPath) {
        adminMapper.updateAvatarPath(id, avatarPath);
    }

    public String getAvatarPath(Integer id) {
        Admin admin = adminMapper.selectById(id);
        return admin != null ? admin.getAvatar() : null;
    }
}
