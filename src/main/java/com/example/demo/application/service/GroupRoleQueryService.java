package com.example.demo.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.GroupRoleService;
import com.example.demo.domain.shared.summary.GroupRoleQueriedSummary;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleQueryService {

	private GroupRoleService groupRoleService;

	/**
	 * 查詢其他(不屬於該群組)的角色
	 * 
	 * @param id      群組ID
	 * @param service 服務
	 * @return
	 */
	public List<GroupRoleQueriedSummary> queryOthers(Long id, String service) {
		return groupRoleService.queryOthers(id, service);
	}
}
