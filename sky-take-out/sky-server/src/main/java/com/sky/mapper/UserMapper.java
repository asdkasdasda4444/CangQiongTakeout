package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("SELECT * FROM user WHERE openid=#{openid}")
    User getByOpenId(String openid);

    /**
     * 添加新用户
     * @param newUser
     */

    void insert(User newUser);
    /**
     * 用户统计
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
