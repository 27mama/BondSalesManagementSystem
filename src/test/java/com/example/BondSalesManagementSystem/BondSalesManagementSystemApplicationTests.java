package com.example.BondSalesManagementSystem;

import com.example.BondSalesManagementSystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BondSalesManagementSystemApplicationTests {
	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		System.out.println(userService.listAll());
	}

}
