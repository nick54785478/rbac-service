package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.share.FunctionInfoQueried;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionQueryService {

	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param service
	 * @param actionType
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String service, String actionType, String type, String name, String activeFlag) {
		List<FunctionInfo> functions = functionInfoRepository.findAllWithSpecification(service, actionType, type, name,
				activeFlag);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}

	/**
	 * 模糊查詢符合條件的群組資料
	 * 
	 * @param queryStr
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String queryStr) {
		List<FunctionInfo> functions = functionInfoRepository.findAllWithSpecification(queryStr);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}
}
