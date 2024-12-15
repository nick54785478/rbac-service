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
	 * @param id
	 * @return
	 */
	public List<RoleFunctionQueried> queryOthers(Long id) {
		return roleFunctionService.queryOthers(id);
	}
}
