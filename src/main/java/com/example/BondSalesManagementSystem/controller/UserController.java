package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;
import com.example.BondSalesManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/allUser", method = RequestMethod.GET)
    public List<User> findAllUser() {
        return userService.listAll();
    }

    @RequestMapping(value = "/regist",method =RequestMethod.POST)
    public Result regist(User user){
        return userService.regist(user);
    }
    /**
     * 登录
     * @param user 参数封装
     * @return Result
     */
    @RequestMapping(value = "/login",method =RequestMethod.POST)
    public Result login(User user){
        return userService.login(user);
    }


}
