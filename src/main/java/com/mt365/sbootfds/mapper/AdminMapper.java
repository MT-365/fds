package com.mt365.sbootfds.mapper;


import com.mt365.sbootfds.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {


    //    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);

    void insert(Admin admin);

    void deleteById(Integer id);

    void updateById(Admin admin);

    Admin selectById(Integer id);

    List<Admin> selectAll(String name);

    void updateAvatarPath(Integer id, String avatarPath);
}
