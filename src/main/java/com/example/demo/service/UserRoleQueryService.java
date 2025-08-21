package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserRoleService;
import com.example.demo.domain.share.UserRoleQueried;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserRoleQueryService {

	private UserRoleService userRoleService;

	
	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	public List<UserRoleQueried> queryRoles(String username, String service) {
		List<UserRoleQueried> roles = userRoleService.queryRoles(username, service);
		log.debug("roles: {}", roles);
		return roles;
	}
	
	
	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username
	 * @param service  服務
	 * @return List<UserRoleGroupQueried>
	 */
	public List<UserRoleQueried> queryOthers(String username, String service) {
		return userRoleService.queryOthers(username, service);

	}
}
