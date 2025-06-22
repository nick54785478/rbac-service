package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.GroupRoleService;
import com.example.demo.domain.share.GroupRoleQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleService groupRoleService;

	/**
	 * 取得特定群組的角色資料
	 * 
	 * @param id      群組 ID
	 * @param service 服務
	 * @return List<GroupRoleQueried>
	 */
	@Transactional
	public List<GroupRoleQueried> queryRoles(Long id, String service) {
		return groupRoleService.queryRoles(id, service);
	}

	/**
	 * 查詢其他(不屬於該角色)的功能
	 * 
	 * @param id      群組 ID
	 * @param service 服務
	 * @return List<GroupRoleQueried>
	 */
	public List<GroupRoleQueried> queryOthers(Long id, String service) {
		return groupRoleService.queryOthers(id, service);
	}
}
