package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.dto.UserGroupQueried;
import com.example.demo.domain.service.UserGroupService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserGroupQueryService {

	private UserGroupService userGroupService;

	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	public List<UserGroupQueried> queryGroups(String username, String service) {
		return userGroupService.queryGroups(username, service);
	}

	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<GroupRoleQueried> 群組角色清單
	 */
	public List<UserGroupQueried> queryOthers(String username, String service) {
		return userGroupService.queryOthers(username, service);

	}
}
