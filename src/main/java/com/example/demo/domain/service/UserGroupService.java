package com.example.demo.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.domain.user.aggregate.entity.UserGroup;
import com.example.demo.domain.user.command.UpdateUserGroupsCommand;
import com.example.demo.exception.ValidationException;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserGroupService {

	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;
	
	/**
	 * 查詢該群組內部不存在的其他角色
	 * 
	 * @param id
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryOthers(String username) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		if (!Objects.isNull(userInfo)) {
			// 篩選出該使用者有的 群組 ID 清單
			List<Long> existingIds = userInfo.getGroups().stream().map(UserGroup::getGroupId)
					.collect(Collectors.toList());

			List<GroupInfo> groups = groupInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該使用者沒有的群組資料
			List<GroupInfo> filtered = groups.stream().filter(e -> !existingIds.contains(e.getId())).collect(Collectors.toList());

			List<UserGroupQueried> userGroups = BaseDataTransformer.transformData(filtered,
					UserGroupQueried.class);
			return userGroups;
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}

	} 
	
	/**
	 * 將使用者加入特定群組
	 * 
	 * @param command
	 * @return UserGroupAdded
	 */
	public void update(UpdateUserGroupsCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());
		List<GroupInfo> groups = groupInfoRepository.findByIdInAndActiveFlag(command.getGroupIds(), YesNo.Y);
		
		// 將群組資料轉為 UserGroup
		List<UserGroup> userGroups =  groups.stream().map(group -> {
			UserGroup userGroup = new UserGroup();
			userGroup.create(group.getId(), userInfo.getId());
			return userGroup;
		}).collect(Collectors.toList());
		
		// 更新群組資料
		userInfo.updateGroups(userGroups);
		userInfoRepository.save(userInfo);
	}
}
