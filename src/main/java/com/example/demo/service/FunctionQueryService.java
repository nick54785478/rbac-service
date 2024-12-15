package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.FunctionService;
import com.example.demo.domain.share.FunctionInfoQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionQueryService {

	private FunctionService functionService;

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param actionType
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String actionType, String type, String name, String activeFlag) {
		return functionService.query(actionType, type, name, activeFlag);
	}
	

	/**
	 * 模糊查詢符合條件的群組資料
	 * 
	 * @param queryStr
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String queryStr) {
		return functionService.query(queryStr);
	}
}
