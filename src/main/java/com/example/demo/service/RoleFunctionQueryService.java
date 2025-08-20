package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.RoleFunctionService;
import com.example.demo.domain.share.RoleFunctionQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleFunctionQueryService {

	private RoleFunctionService roleFunctionService;

	/**
	 * 查詢其他(不屬於該角色)的功能
	 * 
	 * @param id      角色ID
	 * @param service 服務
	 * @return List<RoleFunctionQueried> 角色功能清單
	 */
	public List<RoleFunctionQueried> queryOthers(Long id, String service) {
		return roleFunctionService.queryOthers(id, service);
	}
}
