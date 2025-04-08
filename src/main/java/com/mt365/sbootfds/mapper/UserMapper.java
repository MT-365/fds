package com.mt365.sbootfds.mapper;



import com.mt365.sbootfds.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

    void insert(User user);
}
