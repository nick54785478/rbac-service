package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.share.RoleInfoQueried;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleQueryService {

	private RoleService roleService;
	private RoleInfoRepository roleInfoRepository;

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @return List<RoleInfoQueried>
	 */
	@Transactional
	public List<RoleInfoQueried> query(String type, String name, String activeFlag) {
		List<RoleInfo> roles = roleInfoRepository.findAllWithSpecification(type, name, activeFlag);
		return BaseDataTransformer.transformData(roles, RoleInfoQueried.class);
	}
	
	/**
	 * 透過 ID 查詢角色資料
	 * 
	 * @param id
	 * @return RoleInfoQueried
	 */
	@Transactional
	public RoleInfoQueried query(Long id) {
		return roleService.query(id);
	}
}
