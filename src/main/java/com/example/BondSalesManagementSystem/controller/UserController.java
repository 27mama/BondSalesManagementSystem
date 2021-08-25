package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;
import com.example.BondSalesManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController()
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/allUser", method = RequestMethod.GET)
    public List<User> findAllUser() {
        return userService.listAll();
    }

    @RequestMapping(value = "/regist",method =RequestMethod.POST)
    //@ResponseStatus(code= HttpStatus.CONFLICT,reason="server error")
    public Result regist(@RequestBody HashMap<String, String> userMap){
        User user = new User();
        user.setName(userMap.get("username"));
        user.setPass(userMap.get("password"));
        return userService.regist(user);
    }
    /**
     * 登录
     * @param userMap 参数封装
     * @return Result
     */

    //@ResponseStatus(code= HttpStatus.UNAUTHORIZED,reason="server error")
    @RequestMapping(value = "/login",method =RequestMethod.POST)
    public Result login(@RequestBody HashMap<String, String> userMap){
        User user = new User();
        user.setName(userMap.get("username"));
        user.setPass(userMap.get("password"));
        return userService.login(user);
    }

    @RequestMapping(value = "/logout",method =RequestMethod.GET)
    public Result logout(){
        return userService.logout();
    }

}
