package com.example.demo.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.share.RoleFunctionQueried;
import com.example.demo.domain.share.RoleInfoCreated;
import com.example.demo.domain.share.RoleInfoQueried;
import com.example.demo.domain.share.RoleInfoUpdated;
import com.example.demo.domain.share.RoleOptionQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {

	private FunctionInfoRepository functionInfoRepository;
	private RoleInfoRepository roleInfoRepository;

	/**
	 * 建立角色資訊
	 * 
	 * @param command
	 * @return UserInfoCreated
	 */
	public RoleInfoCreated create(CreateRoleCommand command) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.create(command);
		RoleInfo saved = roleInfoRepository.save(roleInfo);
		return BaseDataTransformer.transformData(saved, RoleInfoCreated.class);
	}

	/**
	 * 建立多筆角色資訊(僅限於前端使用 Inline-Edit)
	 * 
	 * @param command
	 */
	public void createOrUpdate(List<CreateOrUpdateRoleCommand> commands) {

		// 取得 id 清單
		List<Long> ids = commands.stream().filter(command -> command.getId() != null)
				.map(CreateOrUpdateRoleCommand::getId).collect(Collectors.toList());

		// 取出清單相對應資料
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);

		Map<Long, RoleInfo> map = roles.stream().collect(Collectors.toMap(RoleInfo::getId, Function.identity()));

		List<RoleInfo> roleList = commands.stream().map(command -> {

			// 修改
			if (!Objects.isNull(command.getId()) && !Objects.isNull(map.get(command.getId()))) {
				RoleInfo role = map.get(command.getId());
				role.update(command);
				return role;
			} else {
				// 新增
				RoleInfo role = new RoleInfo();
				role.create(command);
				return role;
			}
		}).collect(Collectors.toList());

		roleInfoRepository.saveAll(roleList);
	}

	/**
	 * 更新角色資訊
	 * 
	 * @param command
	 * @return UserInfoCreated
	 */
	public RoleInfoUpdated update(UpdateRoleCommand command) {

		Optional<RoleInfo> opt = roleInfoRepository.findById(command.getId());
		if (opt.isPresent()) {
			RoleInfo roleInfo = opt.get();
			roleInfo.update(command);
			RoleInfo saved = roleInfoRepository.save(roleInfo);
			return BaseDataTransformer.transformData(saved, RoleInfoUpdated.class);
		} else {
			throw new ValidationException("VALIDATION_FAILED", "查無此角色資料 id，更新失敗");
		}
	}

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param id
	 * @return RoleInfoQueried
	 */
	@Transactional
	public RoleInfoQueried query(Long id) {
		Optional<RoleInfo> opt = roleInfoRepository.findById(id);
		if (opt.isPresent()) {
			RoleInfo role = opt.get();
			RoleInfoQueried roleQueried = BaseDataTransformer.transformData(role, RoleInfoQueried.class);
			List<Long> funcIds = role.getFunctions().stream().filter(e -> Objects.equals(e.getActiveFlag(), YesNo.Y))
					.map(RoleFunction::getFunctionId).collect(Collectors.toList());
			List<FunctionInfo> functions = functionInfoRepository.findByIdInAndActiveFlag(funcIds, YesNo.Y);
			List<RoleFunctionQueried> functionRoles = BaseDataTransformer.transformData(functions,
					RoleFunctionQueried.class);
			roleQueried.setFunctions(functionRoles);
			return roleQueried;

		} else {
			throw new ValidationException("VALIDATION_FAILED", "該角色 ID 有誤，查詢失敗");
		}
	}

	/**
	 * 查詢符合條件的角色資料
	 * 
	 * @param type
	 * @param name
	 * @return List<RoleInfoQueried>
	 */
	@Transactional
	public List<RoleInfoQueried> query(String type, String name, String activeFlag) {
		List<RoleInfo> roles = roleInfoRepository.findAllWithSpecification(type, name, activeFlag);
		return BaseDataTransformer.transformData(roles, RoleInfoQueried.class);
	}

	/**
	 * 查詢角色下拉選單資訊
	 * 
	 * @param str 角色資訊字串
	 * @return List<RoleOptionQueried>
	 */
	public List<RoleOptionQueried> getRoleInfoOtions(String str) {
		return BaseDataTransformer.transformData(roleInfoRepository.findAllWithSpecification(str),
				RoleOptionQueried.class);
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 */
	public void delete(List<Long> ids) {
		List<RoleInfo> roles = roleInfoRepository.findByIdIn(ids);
		roles.stream().forEach(role -> {
			role.delete();
		});
		roleInfoRepository.saveAll(roles);
	}

}
