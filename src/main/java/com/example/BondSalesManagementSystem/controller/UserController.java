package com.example.BondSalesManagementSystem.controller;

import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;
import com.example.BondSalesManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
     * @param userMap 参数封装
     * @return Result
     */
    @CrossOrigin
    @RequestMapping(value = "/login",method =RequestMethod.POST)
    public Result login(@RequestBody HashMap<String, String> userMap){
        User user = new User();
        user.setName(userMap.get("username"));
        user.setPass(userMap.get("password"));
        return userService.login(user);
    }


}
