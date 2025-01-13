package com.example.demo.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.function.aggregate.FunctionInfo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.role.aggregate.entity.RoleFunction;
import com.example.demo.domain.share.FunctionInfoDetailQueried;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserInfoCreated;
import com.example.demo.domain.share.UserInfoDetailQueried;
import com.example.demo.domain.share.UserInfoQueried;
import com.example.demo.domain.share.UserInfoUpdated;
import com.example.demo.domain.share.UserOptionQueried;
import com.example.demo.domain.share.UserRoleQueried;
import com.example.demo.domain.share.UserRolesGranted;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.exception.ValidationException;
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
	 * 建立使用者資訊
	 * 
	 * @param command
	 * @return UserInfoCreated
	 */
	public UserInfoCreated create(CreateUserCommand command) {
		UserInfo userInfo = new UserInfo();
		userInfo.create(command);
		UserInfo saved = userRepository.save(userInfo);
		return BaseDataTransformer.transformData(saved, UserInfoCreated.class);
	}

	/**
	 * 查詢該使用者資料
	 * 
	 * @param username
	 * @return UserInfoQueried
	 */
	@Transactional
	public UserInfoQueried queryByUsername(String username) {
		UserInfo userInfo = userRepository.findByUsername(username);
		return BaseDataTransformer.transformData(userInfo, UserInfoQueried.class);
	}

	/**
	 * 更新使用者角色資料
	 * 
	 * @param command
	 * @return UserRoleGranted
	 */
	public UserRolesGranted grant(UpdateUserRolesCommand command) {
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
		UserInfo saved = userRepository.save(user);

		UserRolesGranted userRoleCreated = new UserRolesGranted();
		userRoleCreated.setUsername(saved.getUsername());
		userRoleCreated.setRoles(roleList.stream().map(RoleInfo::getName).collect(Collectors.toList()));
		return userRoleCreated;
	}

	/**
	 * 更新使用者資訊
	 * 
	 * @param command
	 * @return UserInfoCreated
	 */
	public void update(UpdateUserCommand command) {
		Optional<UserInfo> opt = userRepository.findById(command.getId());
		if (opt.isPresent()) {
			var userInfo = opt.get();
			userInfo.update(command);
			userRepository.save(userInfo);
		} else {
			throw new ValidationException("VALIDATION_FAILED", "查無此資料 id，更新失敗");
		}
	}

	/**
	 * 查詢使用者下拉選單資訊
	 * 
	 * @param str 使用者資訊字串
	 * @return UserOtionInfoQueried
	 */
	public List<UserOptionQueried> getUserInfoOtions(String str) {
		return BaseDataTransformer.transformData(userRepository.findAllWithSpecification(str), UserOptionQueried.class);
	}

	/**
	 * 查詢使用者資訊(模糊查詢)
	 * 
	 * @param username 使用者帳號
	 * @return UserOtionInfoQueried
	 */
	public List<UserInfoQueried> getUserInfoList(String username) {
		return BaseDataTransformer.transformData(userRepository.findAllWithSpecification(username),
				UserInfoQueried.class);
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
		List<UserGroupQueried> groups = groupRepository.findByIdInAndActiveFlag(groupIds, YesNo.Y).stream()
				.map(group -> BaseDataTransformer.transformData(group, UserGroupQueried.class))
				.collect(Collectors.toList());
		return groups;
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
		List<UserRoleQueried> userRoleList = roleRepository.findByIdInAndActiveFlag(roleIds, YesNo.Y).stream()
				.map(role -> BaseDataTransformer.transformData(role, UserRoleQueried.class))
				.collect(Collectors.toList());

		return userRoleList;
	}

	/**
	 * 取得使用者詳細資訊
	 * 
	 * @param username
	 * @return UserInfoDetailQueried
	 */
	@Transactional
	public UserInfoDetailQueried getUserDetails(String username) {
		Map<String, List<FunctionInfoDetailQueried>> funcMap = new HashMap<>();

		UserInfo userInfo = userRepository.findByUsername(username);
		// 取得 Group ID 清單
		List<Long> groupIds = userInfo.getGroups().stream().map(UserGroup::getGroupId).collect(Collectors.toList());
		// 取得 Role ID 清單
		List<Long> roleIds = userInfo.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toList());

		List<GroupInfo> groups = groupRepository.findByIdIn(groupIds);
		List<RoleInfo> roles = roleRepository.findByIdIn(roleIds);

		// 取得個人權限
		funcMap.put("PERSONALITY", this.getFuncList(roles));

		// 群組角色權限
		List<Long> groupRoleIds = groups.stream().flatMap(g -> g.getRoles().stream().map(GroupRole::getRoleId))
				.distinct().collect(Collectors.toList());
		List<RoleInfo> groupRoles = roleRepository.findByIdIn(groupRoleIds);
		// 取得群組權限
		funcMap.put("GROUP", this.getFuncList(groupRoles));

		// 合併功能權限(群組角色功能、個人角色功能)
		List<FunctionInfoDetailQueried> functions = funcMap.entrySet().stream().flatMap(entry -> {
			String key = entry.getKey();
			// 遍歷 Functions 資料，將 key 值設置進去
			entry.getValue().forEach(e -> {
				e.setLabel(key);
			});
			return entry.getValue().stream();
		}).collect(Collectors.toList());

		List<UserGroupQueried> groupList = groups.stream()
				.map(group -> BaseDataTransformer.transformData(group, UserGroupQueried.class))
				.collect(Collectors.toList());
		List<UserRoleQueried> roleList = roles.stream()
				.map(role -> BaseDataTransformer.transformData(role, UserRoleQueried.class))
				.collect(Collectors.toList());

		UserInfoDetailQueried resource = BaseDataTransformer.transformData(userInfo, UserInfoDetailQueried.class);
		resource.setRoles(roleList);
		resource.setGroups(groupList);
		resource.setFunctions(functions);
		return resource;

	}

	/**
	 * 透過角色清單取得該角色所屬功能
	 * 
	 * @param roles
	 */
	private List<FunctionInfoDetailQueried> getFuncList(List<RoleInfo> roles) {
		// 個人角色權限 ID 清單
		List<Long> funcIds = roles.stream().flatMap(r -> r.getFunctions().stream().map(RoleFunction::getFunctionId))
				.distinct().collect(Collectors.toList());
		List<FunctionInfo> personalFuncs = functionRepository.findByIdIn(funcIds);
		return BaseDataTransformer.transformData(personalFuncs, FunctionInfoDetailQueried.class);

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
