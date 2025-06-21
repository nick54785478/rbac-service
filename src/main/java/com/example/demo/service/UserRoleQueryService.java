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
	 * 查詢屬於該使用者的角色
	 * 
	 * @param username
	 * @param service
	 * @return List<UserRoleGroupQueried>
	 */
	public List<UserRoleQueried> queryRoles(String username, String service) {
		return userRoleService.queryRoles(username, service);
	}

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username
	 * @param service
	 * @return List<UserRoleGroupQueried>
	 */
	public List<UserRoleQueried> queryOthers(String username, String service) {
		return userRoleService.queryOthers(username, service);

	}
}
