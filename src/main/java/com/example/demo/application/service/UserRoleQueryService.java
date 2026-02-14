package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserRoleService;
import com.example.demo.domain.shared.summary.UserRoleQueriedSummary;

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
	public List<UserRoleQueriedSummary> queryRoles(String username, String service) {
		List<UserRoleQueriedSummary> roles = userRoleService.queryRoles(username, service);
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
	public List<UserRoleQueriedSummary> queryOthers(String username, String service) {
		return userRoleService.queryOthers(username, service);

	}
}
