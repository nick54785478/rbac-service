package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.YesNo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.GroupRoleQueried;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GroupRoleService {

	private RoleInfoRepository roleInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 取得特定群組的角色資料
	 * 
	 * @param groupId 群組 ID
	 * @param service 服務
	 * @return List<UserRoleQueried>
	 */
	@Transactional
	public List<GroupRoleQueried> queryRoles(Long groupId, String service) {
		GroupInfo groupInfo = groupInfoRepository.findById(groupId).orElseThrow(() -> {
			throw new ValidationException("VALIDATE_FAILED", "查無該角色資料， id: " + groupId);
		});

		// 取得該角色的 roleId 清單
		List<Long> roleIds = groupInfo.getRoles().stream()
				// 過濾 UserRole 的 activeFlag = 'N' 者
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(GroupRole::getRoleId).collect(Collectors.toList());
		// 查詢群組角色資料
		return roleInfoRepository.findByIdInAndServiceAndActiveFlag(roleIds, service, YesNo.Y).stream()
				.map(role -> BaseDataTransformer.transformData(role, GroupRoleQueried.class))
				.collect(Collectors.toList());
	}

	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param id
	 * @param service
	 * @return List<GroupRoleQueried>
	 */
	@Transactional
	public List<GroupRoleQueried> queryOthers(Long id, String service) {
		Optional<GroupInfo> opt = groupInfoRepository.findById(id);
		if (opt.isPresent()) {
			GroupInfo group = opt.get();

			// 篩選出該角色有的 Role ID 清單
			List<Long> existingIds = group.getRoles().stream().map(GroupRole::getRoleId).collect(Collectors.toList());

			List<RoleInfo> roles = roleInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該群組所沒有的角色資料
			List<RoleInfo> filtered = roles.stream()
					.filter(e -> !existingIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 過濾出該使用者角色 ActiveFlag = 'N' 的資料
			List<Long> inactiveRelatedIds = group.getRoles().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(GroupRole::getRoleId).collect(Collectors.toList());

			// 過濾出該使用者資料但失效的資料 activeFlag = 'N'
			List<RoleInfo> inactiveRelated = roles.stream()
					.filter(e -> inactiveRelatedIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			return BaseDataTransformer.transformData(filtered, GroupRoleQueried.class);

		} else {
			log.error("該群組 ID 有誤，查詢失敗");
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}

	}

	/**
	 * 更新群組角色
	 * 
	 * @param command
	 */
	public void update(UpdateGroupRolesCommand command) {

		GroupInfo groupInfo = groupInfoRepository.findById(command.getGroupId()).orElseThrow(() -> {
			log.error("查無該角色資料，id:{}", command.getGroupId());
			throw new ValidationException("VALIDATE_FAAILED", "查無該角色資料");
		});

		// 透過 RoleId 清單找出 Role 資料 -> 要被更新的 Role 資料
		List<RoleInfo> roleList = roleInfoRepository.findByIdInAndActiveFlag(command.getRoleIds(), YesNo.Y);

		// 取出非該服務的 Role
		List<RoleInfo> otherRoles = roleInfoRepository.findByServiceNot(command.getService());

		List<GroupRole> groupRoles = this.processUpdatedRoleData(groupInfo, roleList, otherRoles);

		// 變更群組角色
		groupInfo.updateRoles(groupRoles);
		groupInfoRepository.save(groupInfo);

	}

	/**
	 * 處理要被更新的群組角色資料
	 * 
	 * @param groupInfo
	 * @param roles
	 * @param otherRoles
	 */
	private List<GroupRole> processUpdatedRoleData(GroupInfo groupInfo, List<RoleInfo> roles,
			List<RoleInfo> otherRoles) {
		List<RoleInfo> roleList = new ArrayList<>();
		roleList.addAll(roles);
		roleList.addAll(otherRoles);
		// 將角色資料轉為 UserRole
		return roles.stream().map(role -> {
			GroupRole groupRole = new GroupRole();
			groupRole.create(groupInfo.getId(), role.getId());
			return groupRole;
		}).collect(Collectors.toList());
	}
}
