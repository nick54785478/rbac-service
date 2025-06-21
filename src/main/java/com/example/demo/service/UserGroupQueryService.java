package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserGroupService;
import com.example.demo.domain.share.UserGroupQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserGroupQueryService {

	private UserGroupService userGroupService;

	/**
	 * 取得特定使用者所在的 Group 資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried> 使用者群組清單
	 */
	public List<UserGroupQueried> queryGroups(String username, String service) {
		List<UserGroupQueried> groups = userGroupService.queryGroups(username, service);
		return groups;
	}

	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param username
	 * @return List<GroupRoleQueried> 群組角色清單
	 */
	public List<UserGroupQueried> queryOthers(String username, String service) {
		return userGroupService.queryOthers(username, service);

	}
}
