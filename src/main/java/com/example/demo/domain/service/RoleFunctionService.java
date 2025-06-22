package com.example.demo.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.UpdateRoleFunctionsCommand;
import com.example.demo.domain.share.RoleFunctionQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleFunctionService {

	private RoleInfoRepository roleInfoRepository;
	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 查詢該角色不具備的其他功能
	 * 
	 * @param id
	 * @return List<RoleFunctionQueried>
	 */
	@Transactional
	public List<RoleFunctionQueried> queryOthers(Long id) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();

			// 篩選出該角色有的 Function ID 清單
			List<Long> existingIds = role.getFunctions().stream().map(RoleFunction::getFunctionId)
					.collect(Collectors.toList());

			// 查出該角色功能資料清單
			List<FunctionInfo> functions = functionInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該角色所沒有的功能資料
			List<FunctionInfo> filtered = functions.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 過濾出該角色 ActiveFlag = 'N' 的功能資料
			List<Long> inactiveRelatedIds = role.getFunctions().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());

			// 過濾出該角色資料但失效的功能資料 activeFlag = 'N'
			List<FunctionInfo> inactiveRelated = functions.stream().filter(e -> inactiveRelatedIds.contains(e.getId()))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			return BaseDataTransformer.transformData(filtered, RoleFunctionQueried.class);

		} else {
			throw new ValidationException("VALIDATION_FAILED", "該角色 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 賦予角色相關功能權限
	 * 
	 * @param command
	 */
	public void update(UpdateRoleFunctionsCommand command) {
		// 透過功能 ID 清單取得功能
		List<FunctionInfo> functions = functionInfoRepository.findByIdInAndActiveFlag(command.getFunctions(), YesNo.Y);

		// 建立 Role Function 資料清單
		List<RoleFunction> roleFunctions = functions.stream().map(function -> {
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.create(command.getRoleId(), function.getId());
			return roleFunction;
		}).collect(Collectors.toList());

		// 透過 Role Id 取得 角色資料
		roleInfoRepository.findById(command.getRoleId()).ifPresent(role -> {
			role.updateFunctions(roleFunctions);
			roleInfoRepository.save(role);
		});
	}

}
