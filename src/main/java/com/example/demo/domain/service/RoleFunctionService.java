package com.example.demo.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

			List<FunctionInfo> functions = functionInfoRepository.findByActiveFlag(YesNo.Y);

			List<FunctionInfo> filtered = functions.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			List<RoleFunctionQueried> functionRoles = BaseDataTransformer.transformData(filtered,
					RoleFunctionQueried.class);
			return functionRoles;

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
		List<FunctionInfo> functions = functionInfoRepository.findByIdIn(command.getFunctions());

		// 建立 Role Function 資料清單
		List<RoleFunction> roleFunctions = functions.stream().map(function -> {
			RoleFunction roleFunction = new RoleFunction();
			roleFunction.create(command.getRoleId(), function.getId());
			return roleFunction;
		}).collect(Collectors.toList());

		// 透過 Role Id 取的 角色資料
		roleInfoRepository.findById(command.getRoleId()).ifPresent(role -> {
			role.updateFunctions(roleFunctions);
			roleInfoRepository.save(role);
		});
	}

}
