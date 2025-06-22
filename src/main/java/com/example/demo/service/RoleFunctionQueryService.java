package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.RoleFunctionService;
import com.example.demo.domain.share.RoleFunctionQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleFunctionQueryService {

	private RoleFunctionService roleFunctionService;

	/**
	 * 取得特定使用者的資料
	 * 
	 * @param roleId  角色 ID
	 * @param service 服務
	 * @return List<RoleFunctionQueried>
	 */
	@Transactional
	public List<RoleFunctionQueried> queryFuncs(Long roleId, String service) {
		return roleFunctionService.queryRoles(roleId, service);
	}

	/**
	 * 查詢其他(不屬於該角色)的功能
	 * 
	 * @param id
	 * @param service
	 * @return List<RoleFunctionQueried> 角色功能清單
	 */
	public List<RoleFunctionQueried> queryOthers(Long id, String service) {
		return roleFunctionService.queryOthers(id, service);
	}
}
