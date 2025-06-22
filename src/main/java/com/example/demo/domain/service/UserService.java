package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.constant.RoleType;
import com.example.demo.constant.YesNo;
import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.share.FunctionInfoDetailsQueried;
import com.example.demo.domain.share.UserGroupDetailsQueried;
import com.example.demo.domain.share.UserInfoDetailsQueried;
import com.example.demo.domain.share.UserInfoSummaryQueried;
import com.example.demo.domain.share.UserRoleDetailsQueried;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.exception.ValidationException;
import com.example.demo.iface.dto.FunctionInfoSummaryQueried;
import com.example.demo.infra.assembler.UserAssembler;
import com.example.demo.infra.repository.FunctionInfoRepository;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private UserAssembler userDetailsAssembler;
	private GroupInfoRepository groupRepository;
	private UserInfoRepository userRepository;
	private RoleInfoRepository roleRepository;
	private FunctionInfoRepository functionRepository;

	/**
	 * 更新使用者角色資料
	 * 
	 * @param command
	 * @return UserRoleGranted
	 */
	public void grant(UpdateUserRolesCommand command) {
		UserInfo user = userRepository.findByUsername(command.getUsername());
		if (Objects.isNull(user)) {
			log.error("該使用者名稱不合法");
			throw new ValidationException("VALIDATION_FAILED", "該使用者名稱不合法");
		}

		List<RoleInfo> roleList = roleRepository.findByIdIn(command.getRoleIds());

		List<UserRole> userRoles = roleList.stream().map(role -> {
			UserRole userRole = new UserRole();
			userRole.create(user.getId(), role.getId());
			return userRole;
		}).collect(Collectors.toList());

		user.updateRoles(userRoles);
		userRepository.save(user);

	}

	/**
	 * 取得所有服務的使用者詳細資訊
	 * 
	 * @param username
	 * @return UserInfoSummaryQueried
	 */
	@Transactional
	public UserInfoSummaryQueried getUserSummary(String username) {

		UserInfo userInfo = userRepository.findByUsername(username);

		// 取得 Role ID 清單
		List<Long> roleIds = userInfo.getRoles().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserRole::getRoleId).collect(Collectors.toList());

		// 取得個人角色
		List<RoleInfo> roles = roleRepository.findByIdInAndActiveFlag(roleIds, YesNo.Y);

		// 取得 Group ID 清單
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		List<GroupInfo> groups = groupRepository.findByIdInAndActiveFlag(groupIds, YesNo.Y);

		return userDetailsAssembler.assemblerUserInfoSummary(userInfo, roles, groups);
	}

	/**
	 * 取得特定服務的使用者詳細資訊
	 * 
	 * @param username
	 * @param service
	 * @return UserInfoDetailQueried
	 */
	@Transactional
	public UserInfoDetailsQueried getUserDetails(String username, String service) {

		Map<String, List<FunctionInfoDetailsQueried>> funcMap = new HashMap<>();

		UserInfo userInfo = userRepository.findByUsername(username);

		// 取得 Role ID 清單
		List<Long> roleIds = userInfo.getRoles().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserRole::getRoleId).collect(Collectors.toList());

		// 取得個人角色
		List<RoleInfo> personalRoles = roleRepository.findByIdInAndActiveFlag(roleIds, YesNo.Y);

		// 取得個人權限
		funcMap.put(RoleType.PERSONALITY.getName(), this.getFuncList(personalRoles));

		// 取得 Group ID 清單
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		List<GroupInfo> groups = groupRepository.findByIdInAndActiveFlag(groupIds, YesNo.Y);

		// 取得群組角色
		List<Long> groupRoleIds = groups.stream().flatMap(g -> g.getRoles().stream().map(GroupRole::getRoleId))
				.distinct().collect(Collectors.toList());
		List<RoleInfo> groupRoles = roleRepository.findByIdInAndActiveFlag(groupRoleIds, YesNo.Y);
		// 取得群組權限
		funcMap.put(RoleType.GROUP.getName(), this.getFuncList(groupRoles));

		// 合併功能權限(群組角色功能、個人角色功能)
		List<FunctionInfoDetailsQueried> functions = funcMap.entrySet().stream().flatMap(entry -> {
			String key = entry.getKey();
			return entry.getValue().stream().filter(e -> StringUtils.equals(service, e.getService())) // 過濾指定 service
					.peek(e -> e.setLabel(RoleType.getValueByName(key))); // 設定 label
		}).collect(Collectors.toList());

		// 群組清單
		List<UserGroupDetailsQueried> groupList = groups.stream()
				.filter(group -> StringUtils.equals(group.getService(), service))
				.map(group -> BaseDataTransformer.transformData(group, UserGroupDetailsQueried.class))
				.collect(Collectors.toList());

		// 透過個人角色資料及群組角色資料取得 RoleList
		List<UserRoleDetailsQueried> roleList = this.getRoleList(service, personalRoles, groupRoles);
		return userDetailsAssembler.assembleUserInfoDetails(userInfo, roleList, groupList, functions);
	}

	/**
	 * 透過個人角色資料及群組角色資料取得 RoleList
	 * 
	 * @param service
	 * @param personalRoles
	 * @return groupRoles
	 */
	private List<UserRoleDetailsQueried> getRoleList(String service, List<RoleInfo> personalRoles,
			List<RoleInfo> groupRoles) {
		List<UserRoleDetailsQueried> roleList = new ArrayList<>();
		// 個人角色處理
		List<UserRoleDetailsQueried> personaRoleDetailsList = personalRoles.stream()
				.filter(role -> StringUtils.equals(service, role.getService())).map(role -> {
					UserRoleDetailsQueried roleQueried = BaseDataTransformer.transformData(role,
							UserRoleDetailsQueried.class);
					roleQueried.setRoleType(RoleType.PERSONALITY.getValue());
					return roleQueried;
				}).collect(Collectors.toList());
		roleList.addAll(personaRoleDetailsList);

		List<UserRoleDetailsQueried> groupRoleDetailsList = groupRoles.stream()
				.filter(role -> StringUtils.equals(service, role.getService())).map(role -> {
					UserRoleDetailsQueried roleQueried = BaseDataTransformer.transformData(role,
							UserRoleDetailsQueried.class);
					roleQueried.setRoleType(RoleType.GROUP.getValue());
					return roleQueried;
				}).collect(Collectors.toList());
		// 群組角色清單
		roleList.addAll(groupRoleDetailsList);
		return roleList;
	}

	/**
	 * 透過角色清單取得該角色所屬功能
	 * 
	 * @param roles
	 * @return List<FunctionInfoDetailQueried>
	 */
	private List<FunctionInfoDetailsQueried> getFuncList(List<RoleInfo> roles) {
		// 個人角色權限 ID 清單
		List<Long> funcIds = roles.stream().flatMap(r -> r.getFunctions().stream().map(RoleFunction::getFunctionId))
				.distinct().collect(Collectors.toList());
		List<FunctionInfo> functionList = functionRepository.findByIdIn(funcIds);
		return BaseDataTransformer.transformData(functionList, FunctionInfoDetailsQueried.class);
	}

	/**
	 * 檢查該帳號、身分證、email 是否已註冊
	 * 
	 * @author command
	 * @return boolean
	 */
	public boolean checkIsRegistered(CreateUserCommand command) {
		List<UserInfo> userList = userRepository.findByUsernameOrNationalIdNoOrEmail(command.getUsername(),
				command.getNationalId(), command.getEmail());
		return userList.isEmpty();
	}
}
