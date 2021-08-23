package com.example.BondSalesManagementSystem.service;

import com.example.BondSalesManagementSystem.model.Result;
import com.example.BondSalesManagementSystem.model.User;

import java.util.List;

public interface UserService {
    /**
     * 列出所有的user
     * @return users
     */
    List<User> listAll();

    Result regist(User user);

    Result login(User user);
}
