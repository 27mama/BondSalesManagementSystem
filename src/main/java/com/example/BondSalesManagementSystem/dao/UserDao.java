package com.example.BondSalesManagementSystem.dao;

import com.example.BondSalesManagementSystem.mapper.UserMapper;
import com.example.BondSalesManagementSystem.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao extends UserMapper {
    @Select("SELECT * FROM USER")
    public List<User> findAll();

    /**
     * 查询用户名是否存在，若存在，不允许注册
     * @param name
     */
    @Select(value = "select u.name,u.pass from user u where u.name=#{name}")
    @Results
            ({@Result(property = "name",column = "name"),
                    @Result(property = "pass",column = "pass")})
    User findUserByName(@Param("name") String name);

    /**
     * 注册  插入一条user记录
     * @param user
     */
    @Insert("insert into user (name,pass) values(#{name},#{pass})")
    void regist(User user);

    /**
     * 登录
     * @param user
     */
    @Select("select u.id from user u where u.name = #{name} and pass = #{pass}")
    Integer login(User user);

}
