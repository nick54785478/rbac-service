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
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param username
	 * @return List<GroupRoleQueried>
	 */
	public List<UserGroupQueried> queryOthers(String username) {
		return userGroupService.queryOthers(username);

	}
}
