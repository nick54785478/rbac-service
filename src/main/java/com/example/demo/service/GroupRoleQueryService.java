package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.GroupRoleService;
import com.example.demo.domain.share.GroupRoleQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleService groupRoleService;
	
	/**
	 * 查詢其他(不屬於該角色)的功能
	 * 
	 * @param id
	 * @return
	 */
	public List<GroupRoleQueried> queryOthers(Long id) {
		return groupRoleService.queryOthers(id);
	}
}
