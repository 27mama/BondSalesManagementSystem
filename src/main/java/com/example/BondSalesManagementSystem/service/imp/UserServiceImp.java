package com.example.BondSalesManagementSystem.service.imp;

import com.example.BondSalesManagementSystem.dao.UserDao;
import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;
import com.example.BondSalesManagementSystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class UserServiceImp implements UserService {
    @Autowired
    private UserDao userDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<User> listAll() {
        return userDao.findAll();
    }

    /**
     * 注册
     * @param user 参数封装
     * @return Result
     */
    public Result regist(User user) {
        Result result = new Result();
        result.setSuccess(false);
        result.setUser(null);
        try {
            User existUser = userDao.findUserByName(user.getName());
            if(existUser != null){
                //如果用户名已存在
                result.setMsg("用户名已存在");

            }else{
                userDao.regist(user);
                //System.out.println(user.getId());
                result.setMsg("注册成功");
                result.setSuccess(true);
                result.setUser(user);

            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            logger.error("error", e);
        }
        return result;
    }
    /**
     * 登录
     * @param user 用户名和密码
     * @return Result
     */
    public Result login(User user) {
        Result result = new Result();
        result.setSuccess(false);
        result.setUser(null);
        try {
            Integer userId= userDao.login(user);
            if(userId == null){
                result.setMsg("用户名或密码错误");
            }else{
                result.setMsg("登录成功");
                result.setSuccess(true);
                user.setId(userId);
                result.setUser(user);
                String md5 = DigestUtils.md5DigestAsHex(userId.toString().getBytes(StandardCharsets.UTF_8));
                result.setToken(md5);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            logger.error("error", e);
        }
        return result;
    }


    public Result logout() {
        Result result = new Result();
        result.setSuccess(true);
        result.setUser(null);
        return result;
    }

}
