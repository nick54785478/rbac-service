package com.example.demo.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.group.aggregate.entity.GroupRole;
import com.example.demo.domain.group.command.UpdateGroupRolesCommand;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.GroupRoleQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupRoleService {

	private RoleInfoRepository roleInfoRepository;
	private GroupInfoRepository groupInfoRepository;
	
	
	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param id
	 * @return List<GroupRoleQueried>
	 */
	@Transactional
	public List<GroupRoleQueried> queryOthers(Long id) {
		Optional<GroupInfo> opt = groupInfoRepository.findById(id);
		if (opt.isPresent()) {
			GroupInfo group = opt.get();

			// 篩選出該角色有的 Function ID 清單
			List<Long> existingIds = group.getRoles().stream().map(GroupRole::getRoleId)
					.collect(Collectors.toList());

			List<RoleInfo> roles = roleInfoRepository.findByActiveFlag(YesNo.Y);

			List<RoleInfo> filtered = roles.stream().filter(e -> !existingIds.contains(e.getId())).collect(Collectors.toList());

			List<GroupRoleQueried> groupRoles = BaseDataTransformer.transformData(filtered,
					GroupRoleQueried.class);
			return groupRoles;

		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}

	}
	
	/**
	 * 更新群組角色
	 * 
	 * @param command
	 */
	public void update(UpdateGroupRolesCommand command) {
		// 透過 Role id 清單找出 Role 資料
		List<RoleInfo> roleList = roleInfoRepository.findByIdInAndActiveFlag(command.getRoleIds(), YesNo.Y);
		// 透過 group id 找到 Group 資料
		groupInfoRepository.findById(command.getGroupId()).ifPresent(group -> {
			List<GroupRole> groupRoles = roleList.stream().map(role -> {
				GroupRole groupRole = new GroupRole();
				groupRole.create(group.getId(), role.getId());
				return groupRole;
			}).collect(Collectors.toList());

			// 變更群組角色
			group.updateRoles(groupRoles);
			groupInfoRepository.save(group);
		});
	}
	
}
