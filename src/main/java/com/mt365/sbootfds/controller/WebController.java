package com.mt365.sbootfds.controller;


import com.mt365.sbootfds.common.Result;
import com.mt365.sbootfds.common.RoleEnum;
import com.mt365.sbootfds.entity.Account;
import com.mt365.sbootfds.entity.User;
import com.mt365.sbootfds.service.AdminService;
import com.mt365.sbootfds.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if(RoleEnum.ADMIN.name().equals(account.getRole())){
            account = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        }
        else {
            return Result.error("角色不存在");
        }
        return Result.success(account);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if(RoleEnum.USER.name().equals(user.getRole())){
            userService.register(user);
        }
        else {
            return Result.error("你的角色不可以注册");
        }
        return Result.success();
    }
}
