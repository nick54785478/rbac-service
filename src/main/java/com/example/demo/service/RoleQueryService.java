package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.share.RoleInfoQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleQueryService {

	private RoleService roleService;

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @return List<RoleInfoQueried>
	 */
	public List<RoleInfoQueried> query(String type, String name, String activeFlag) {
		return roleService.query(type, name, activeFlag);
	}
	
	/**
	 * 透過 ID 查詢角色資料
	 * 
	 * @param id
	 * @return RoleInfoQueried
	 */
	public RoleInfoQueried query(Long id) {
		return roleService.query(id);
	}
}
