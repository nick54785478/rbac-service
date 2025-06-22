package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.YesNo;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.UpdateRoleFunctionsCommand;
import com.example.demo.domain.share.RoleFunctionQueried;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RoleFunctionService {

	private RoleInfoRepository roleInfoRepository;
	private FunctionInfoRepository functionInfoRepository;

	/**
	 * 取得特定角色的功能資料
	 * 
	 * @param roleId  角色 ID
	 * @param service 服務
	 * @return List<UserRoleQueried>
	 */
	@Transactional
	public List<RoleFunctionQueried> queryRoles(Long roleId, String service) {
		RoleInfo roleInfo = roleInfoRepository.findById(roleId).orElseThrow(() -> {
			throw new ValidationException("VALIDATE_FAILED", "查無該角色資料， id: " + roleId);
		});

		// 取得該角色的 funcId 清單
		List<Long> funcIds = roleInfo.getFunctions().stream()
				// 過濾 UserRole 的 activeFlag = 'N' 者
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(RoleFunction::getFunctionId).collect(Collectors.toList());
		// 查詢使用者角色資料
		return functionInfoRepository.findByIdInAndServiceAndActiveFlag(funcIds, service, YesNo.Y).stream()
				.map(func -> BaseDataTransformer.transformData(func, RoleFunctionQueried.class))
				.collect(Collectors.toList());
	}

	/**
	 * 查詢該角色不具備的其他功能
	 * 
	 * @param id      角色 ID
	 * @param service 服務
	 * @return List<RoleFunctionQueried>
	 */
	@Transactional
	public List<RoleFunctionQueried> queryOthers(Long id, String service) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();

			// 篩選出該角色有的 Function ID 清單
			List<Long> existingIds = role.getFunctions().stream().map(RoleFunction::getFunctionId)
					.collect(Collectors.toList());

			// 查出該角色功能資料清單
			List<FunctionInfo> functions = functionInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該角色所沒有的功能資料
			List<FunctionInfo> filtered = functions.stream()
					.filter(e -> !existingIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 過濾出該角色 ActiveFlag = 'N' 的功能資料
			List<Long> inactiveRelatedIds = role.getFunctions().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());

			// 過濾出該角色資料但失效的功能資料 activeFlag = 'N'
			List<FunctionInfo> inactiveRelated = functions.stream()
					.filter(e -> inactiveRelatedIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			return BaseDataTransformer.transformData(filtered, RoleFunctionQueried.class);

		} else {
			log.error("該角色 ID 有誤，查詢失敗");
			throw new ValidationException("VALIDATION_FAILED", "該角色 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 賦予角色相關功能權限
	 * 
	 * @param command
	 */
	public void update(UpdateRoleFunctionsCommand command) {

		RoleInfo roleInfo = roleInfoRepository.findById(command.getRoleId()).orElseThrow(() -> {
			log.error("查無該角色資料，id:{}", command.getRoleId());
			throw new ValidationException("VALIDATE_FAAILED", "查無該角色資料");
		});

		// 透過功能 ID 清單取得功能 -> 要被更新的 Function 資料
		List<FunctionInfo> functions = functionInfoRepository.findByIdInAndActiveFlag(command.getFunctions(), YesNo.Y);

		// 取出非該服務的 Function
		List<FunctionInfo> otherFunctions = functionInfoRepository.findByServiceNot(command.getService());

		// 處理要被更新的 Role 資料
		List<RoleFunction> roleFunctions = this.processUpdatedFuncData(roleInfo, functions, otherFunctions);
		
		roleInfo.updateFunctions(roleFunctions);
		roleInfoRepository.save(roleInfo);

	}

	/**
	 * 處理要被更新的 Role 資料
	 * 
	 * @param userInfo
	 * @param roles
	 * @param otherRoles
	 */
	private List<RoleFunction> processUpdatedFuncData(RoleInfo roleInfo, List<FunctionInfo> functions,
			List<FunctionInfo> otherFunctions) {
		List<FunctionInfo> funcList = new ArrayList<>();
		funcList.addAll(functions);
		funcList.addAll(otherFunctions);
		// 將角色資料轉為 UserRole
		return funcList.stream().map(function -> {
			RoleFunction roleFunc = new RoleFunction();
			roleFunc.create(roleInfo.getId(), function.getId());
			return roleFunc;
		}).collect(Collectors.toList());
	}

}
