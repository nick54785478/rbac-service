package com.example.demo.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.share.FunctionInfoDetailsQueried;
import com.example.demo.domain.share.UserGroupDetailsQueried;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserInfoDetailsQueried;
import com.example.demo.domain.share.UserRoleDetailsQueried;
import com.example.demo.domain.share.UserRoleQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.infra.exception.ValidationException;
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
	 * 取得特定使用者所在的群組資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryGroups(String username) {
		UserInfo userInfo = userRepository.findByUsername(username);
		// 取得 User Group 的 GroupId
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		// 透過 ID 取得 Group 資料
		return groupRepository.findByIdInAndActiveFlag(groupIds, YesNo.Y).stream()
				.map(group -> BaseDataTransformer.transformData(group, UserGroupQueried.class))
				.collect(Collectors.toList());
	}

	/**
	 * 取得特定使用者的角色資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserRoleQueried>
	 */
	@Transactional
	public List<UserRoleQueried> queryRoles(String username) {
		UserInfo user = userRepository.findByUsername(username);
		// 取得該使用者的 RoleId 清單
		List<Long> roleIds = user.getRoles().stream()
				// 過濾 UserRole 的 activeFlag = 'N' 者
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserRole::getRoleId).collect(Collectors.toList());

		// 查詢使用者角色資料
		return roleRepository.findByIdInAndActiveFlag(roleIds, YesNo.Y).stream()
				.map(role -> BaseDataTransformer.transformData(role, UserRoleQueried.class))
				.collect(Collectors.toList());

	}

	/**
	 * 取得使用者詳細資訊
	 * 
	 * @param username 使用者名稱
	 * @param service  服務
	 * @return UserInfoDetailQueried
	 */
	@Transactional
	public UserInfoDetailsQueried getUserDetails(String username, String service) {
		Map<String, List<FunctionInfoDetailsQueried>> funcMap = new HashMap<>();

		UserInfo userInfo = userRepository.findByUsername(username);
		// 取得 Group ID 清單
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		// 取得 Role ID 清單
		List<Long> roleIds = userInfo.getRoles().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserRole::getRoleId).collect(Collectors.toList());

		List<GroupInfo> groups = groupRepository.findByIdInAndServiceAndActiveFlag(groupIds, service, YesNo.Y);
		List<RoleInfo> roles = roleRepository.findByIdInAndServiceAndActiveFlag(roleIds, service, YesNo.Y);

		// 取得個人權限
		funcMap.put("PERSONALITY", this.getFuncList(roles));

		// 群組角色權限
		List<Long> groupRoleIds = groups.stream().flatMap(g -> g.getRoles().stream().map(GroupRole::getRoleId))
				.distinct().collect(Collectors.toList());
		List<RoleInfo> groupRoles = roleRepository.findByIdInAndServiceAndActiveFlag(groupRoleIds, service, YesNo.Y);
		// 取得群組權限
		funcMap.put("GROUP", this.getFuncList(groupRoles));

		// 合併功能權限(群組角色功能、個人角色功能)
		List<FunctionInfoDetailsQueried> functions = funcMap.entrySet().stream().flatMap(entry -> {
			String key = entry.getKey();
			// 遍歷 Functions 資料，將 key 值設置進去
			entry.getValue().forEach(e -> {
				e.setLabel(key);
			});
			return entry.getValue().stream();
		}).collect(Collectors.toList());

		List<UserGroupDetailsQueried> groupList = groups.stream()
				.map(group -> BaseDataTransformer.transformData(group, UserGroupDetailsQueried.class))
				.collect(Collectors.toList());
		List<UserRoleDetailsQueried> roleList = roles.stream()
				.map(role -> BaseDataTransformer.transformData(role, UserRoleDetailsQueried.class))
				.collect(Collectors.toList());

		UserInfoDetailsQueried resource = BaseDataTransformer.transformData(userInfo, UserInfoDetailsQueried.class);
		resource.setRoles(roleList);
		resource.setGroups(groupList);
		resource.setFunctions(functions);
		return resource;

	}

	/**
	 * 透過角色清單取得該角色所屬功能
	 * 
	 * @param roles
	 * @return List<FunctionInfoDetailsQueried>
	 */
	private List<FunctionInfoDetailsQueried> getFuncList(List<RoleInfo> roles) {
		// 個人角色權限 ID 清單
		List<Long> funcIds = roles.stream().flatMap(r -> r.getFunctions().stream().map(RoleFunction::getFunctionId))
				.distinct().collect(Collectors.toList());
		List<FunctionInfo> personalFuncs = functionRepository.findByIdIn(funcIds);
		return BaseDataTransformer.transformData(personalFuncs, FunctionInfoDetailsQueried.class);

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
