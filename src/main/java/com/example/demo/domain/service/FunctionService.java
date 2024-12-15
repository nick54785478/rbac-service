package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.function.command.CreateFunctionCommand;
import com.example.demo.domain.function.command.CreateOrUpdateFunctionCommand;
import com.example.demo.domain.share.FunctionCreated;
import com.example.demo.domain.share.FunctionInfoQueried;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FunctionService {

	private FunctionInfoRepository functionRepository;

	/**
	 * 建立一筆功能資料
	 * 
	 * @param command
	 * @return FunctionCreated
	 */
	public FunctionCreated create(CreateFunctionCommand command) {
		FunctionInfo function = new FunctionInfo();
		function.create(command);
		FunctionInfo saved = functionRepository.save(function);
		return BaseDataTransformer.transformData(saved, FunctionCreated.class);
	}

	/**
	 * 建立多筆功能資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param command
	 */
	public void createOrUpdate(List<CreateOrUpdateFunctionCommand> commands) {
		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(CreateOrUpdateFunctionCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);

		Map<Long, FunctionInfo> map = functions.stream()
				.collect(Collectors.toMap(FunctionInfo::getId, Function.identity()));
		List<FunctionInfo> functionList = commands.stream().map(command -> {
			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				FunctionInfo function = map.get(command.getId());
				function.update(command);
				return function;
			} else {
				// 新增
				FunctionInfo function = new FunctionInfo();
				function.create(command);
				return function;
			}
		}).collect(Collectors.toList());
		functionRepository.saveAll(functionList);
	}

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
		List<FunctionInfo> functions = functionRepository.findAllWithSpecification(actionType, type, name, activeFlag);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}
	
	/**
	 * 模糊查詢符合條件的群組資料
	 * 
	 * @param queryStr
	 * @return List<GroupInfoQueried>
	 */
	public List<FunctionInfoQueried> query(String queryStr) {
		List<FunctionInfo> functions = functionRepository.findAllWithSpecification(queryStr);
		return BaseDataTransformer.transformData(functions, FunctionInfoQueried.class);
	}

	/**
	 * 刪除多筆功能資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<FunctionInfo> functions = functionRepository.findByIdIn(ids);
		functions.stream().forEach(function -> {
			function.delete();
		});
		functionRepository.saveAll(functions);
	}
	
}
