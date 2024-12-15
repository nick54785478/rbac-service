package com.example.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserOptionQueried;
import com.example.demo.domain.share.UserRoleQueried;

@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserService userService;

	/**
	 * 查詢使用者群組
	 */
	@Test
	void testQueryGroups() {
		String username = "nick123@example.com";
		List<UserGroupQueried> queryGroups = userService.queryGroups(username);
		System.out.println(queryGroups);
		assertFalse(queryGroups.isEmpty());
	}

	/**
	 * 查詢使用者角色
	 */
	@Test
	void testQueryRoles() {
		String username = "max123@example.com";
		List<UserRoleQueried> queryRoles = userService.queryRoles(username);
		System.out.println(queryRoles);
		assertFalse(queryRoles.isEmpty());

	}
	
	/**
	 * 
	 * */
	@Test
	void testGetUserInfoList() {
		List<UserOptionQueried> options = userService.getUserInfoOtions("nick");
		System.out.println(options);
		assertTrue(!options.isEmpty());

	}

}
