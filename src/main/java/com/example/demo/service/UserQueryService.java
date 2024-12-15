package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserInfoDetailQueried;
import com.example.demo.domain.share.UserInfoQueried;
import com.example.demo.domain.share.UserRoleQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserQueryService {

	private UserService userService;

	/**
	 * 取得特定使用者所在的 Group 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserGroupQueried>
	 */
	public List<UserGroupQueried> queryGroups(String username) {
		List<UserGroupQueried> groups = userService.queryGroups(username);
		return groups;
	}
	
	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	public List<UserRoleQueried> queryRoles(String username) {
		List<UserRoleQueried> roles = userService.queryRoles(username);
		return roles;
	}

	/**
	 * 查詢該使用者資訊
	 * 
	 * @param username 使用者帳號
	 * @return UserInfoQueried
	 */
	public List<UserInfoQueried> query(String username) {
		return userService.getUserInfoList(username);
	}
	
	/**
	 * 取得使用者詳細資訊
	 * 
	 * @param username
	 * @return UserInfoQueried
	 */
	public UserInfoDetailQueried getUserDetails(String username) {
		return userService.getUserDetails(username);
	}

}
