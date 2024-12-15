package com.example.demo.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.UserRoleQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.aggregate.entity.UserRole;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleService {

	private RoleInfoRepository roleInfoRepository;
	private UserInfoRepository userInfoRepository;

	/**
	 * 查詢不屬於該使用者的其他角色
	 * 
	 * @param username
	 * @return List<UserRoleGroupQueried>
	 */
	@Transactional
	public List<UserRoleQueried> queryOthers(String username) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		if (!Objects.isNull(userInfo)) {
			// 篩選出該使用者有的 群組 ID 清單
			List<Long> existingIds = userInfo.getGroups().stream().map(UserGroup::getGroupId)
					.collect(Collectors.toList());

			List<RoleInfo> roles = roleInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該使用者沒有的群組資料
			List<RoleInfo> filtered = roles.stream().filter(e -> !existingIds.contains(e.getId()))
					.collect(Collectors.toList());

			List<UserRoleQueried> userRoles = BaseDataTransformer.transformData(filtered, UserRoleQueried.class);
			return userRoles;
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 更新使用者角色權限
	 * 
	 * @param command
	 */
	public void update(UpdateUserRolesCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());
		List<RoleInfo> roles = roleInfoRepository.findByIdInAndActiveFlag(command.getRoleIds(), YesNo.Y);

		// 將角色資料轉為 UserRole
		List<UserRole> userRoles = roles.stream().map(role -> {
			UserRole userRole = new UserRole();
			userRole.create(userInfo.getId(), role.getId());
			return userRole;
		}).collect(Collectors.toList());

		// 更新群組資料
		userInfo.updateRoles(userRoles);
		userInfoRepository.save(userInfo);
	}
}
