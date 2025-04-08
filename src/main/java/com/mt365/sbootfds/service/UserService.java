package com.mt365.sbootfds.service;


import cn.hutool.core.util.ObjectUtil;

import com.mt365.sbootfds.common.RoleEnum;
import com.mt365.sbootfds.entity.Account;
import com.mt365.sbootfds.entity.User;
import com.mt365.sbootfds.exception.CustomException;
import com.mt365.sbootfds.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public Account login(Account account) {
        String username = account.getUsername();
        //根据账号查询用户信息
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser == null) {
            throw new CustomException("账号不存在");
        }
        //校验密码
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomException("账号或者密码错误");
        }
        return dbUser;
    }

    public void register(User user){
        //1. 校验账号是否存在
        String username = user.getUsername();
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser != null) {
            throw new CustomException("账号已经存在");
        }
        //2.校验密码
        if (ObjectUtil.isEmpty(user.getPassword())) {
            throw new CustomException("密码不能为空");
        }
        if(ObjectUtil.isEmpty(user.getName())){
            user.setName(user.getUsername());//如果没有设置昵称，就使用用户名
        }
        user.setRole(RoleEnum.USER.name());
        //3. 保存用户信息
        userMapper.insert(user);
    }
}
