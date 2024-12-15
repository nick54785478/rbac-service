package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserRoleService;
import com.example.demo.domain.share.UserRoleQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleQueryService {

	private UserRoleService userRoleService;
	
	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username
	 * @return List<UserRoleGroupQueried>
	 */
	public List<UserRoleQueried> queryOthers(String username) {
		return userRoleService.queryOthers(username);

	}
}
