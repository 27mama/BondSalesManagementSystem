package com.example.BondSalesManagementSystem;

import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;
import com.example.BondSalesManagementSystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test
    @Transactional
    void login() {
        User user = new User();
        user.setName("Sam");
        user.setPass("456");
        Result res = userService.login(user);
        System.out.println("提示信息:" + res.getMsg());
        //Assert.assertTrue(user.getId()>0);
    }

    @Test
    @Transactional
    void login2() {
        User user = new User();
        user.setName("Sam");
        user.setPass("123");
        Result res = userService.login(user);
        System.out.println("提示信息:" + res.getMsg());
        //Assert.assertTrue(user.getId()>0);
    }

    @Test
    @Transactional
    void regist() {
        User user = new User();
        user.setName("Sam");
        user.setPass("456");
        Result res = userService.regist(user);
        System.out.println("提示信息:" + res.getMsg());
        //Assert.assertTrue(user.getId()>0);
    }

    @Test
    @Transactional
    void regist2() {
        User user = new User();
        user.setName("Xu");
        user.setPass("456");
        Result res = userService.regist(user);
        System.out.println("提示信息:" + res.getMsg());
        //Assert.assertTrue(user.getId()>0);
    }
}
