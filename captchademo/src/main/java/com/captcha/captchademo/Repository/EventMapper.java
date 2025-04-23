package com.captcha.captchademo.Repository;

import com.captcha.captchademo.Entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EventMapper {

    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    // Find user by username
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    // Save a new user (insert)
    @Insert("INSERT INTO user (username, email, password) VALUES (#{username}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);

    // Update an existing user
    @Update("UPDATE user SET username = #{username}, email = #{email}, password = #{password} WHERE email = #{email}")
    void update(User user);

    // You can add more methods as needed, for example, to delete users or list all users

}
