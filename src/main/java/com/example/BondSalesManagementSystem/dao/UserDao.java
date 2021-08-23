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
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param name
     * @return
     */
    @Select(value = "select u.name,u.pass from user u where u.name=#{name}")
    @Results
            ({@Result(property = "name",column = "name"),
                    @Result(property = "pass",column = "pass")})
    User findUserByName(@Param("name") String name);

    /**
     * 注册  插入一条user记录
     * @param user
     * @return
     */
    @Insert("insert into user values(#{id},#{name},#{pass})")
    //加入该注解可以保存对象后，查看对象插入id
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void regist(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    @Select("select u.id from user u where u.name = #{name} and pass = #{pass}")
    Integer login(User user);

}
