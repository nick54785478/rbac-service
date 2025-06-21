package com.example.demo.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.YesNo;
import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.share.UserGroupQueried;
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
	 * 取得特定使用者所在的群組資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryGroups(String username, String service) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		// 取得 User Group 的 GroupId
		List<Long> groupIds = userInfo.getGroups().stream()
				.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.Y.getValue()))
				.map(UserGroup::getGroupId).collect(Collectors.toList());
		// 透過 ID 取得 Group 資料
		return groupInfoRepository.findByIdInAndServiceAndActiveFlag(groupIds, service, YesNo.Y).stream()
				.map(group -> BaseDataTransformer.transformData(group, UserGroupQueried.class))
				.collect(Collectors.toList());
	}

	/**
	 * 查詢不屬於該使用者的群組
	 * 
	 * @param username
	 * @param service
	 * @return List<UserGroupQueried>
	 */
	@Transactional
	public List<UserGroupQueried> queryOthers(String username, String service) {
		UserInfo userInfo = userInfoRepository.findByUsername(username);

		if (!Objects.isNull(userInfo)) {

			// 篩選出該使用者有的 群組 ID 清單
			List<Long> existingIds = userInfo.getGroups().stream().map(UserGroup::getGroupId)
					.collect(Collectors.toList());

			List<GroupInfo> groups = groupInfoRepository.findByActiveFlag(YesNo.Y);

			// 過濾出該使用者沒有的群組資料
			List<GroupInfo> filtered = groups.stream()
					.filter(e -> !existingIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 過濾出該使用者角色 ActiveFlag = 'N' 的資料
			List<Long> inactiveRelatedIds = userInfo.getGroups().stream()
					.filter(e -> StringUtils.equals(e.getActiveFlag().getValue(), YesNo.N.getValue()))
					.map(UserGroup::getGroupId).collect(Collectors.toList());

			// 過濾出該使用者資料但失效的資料 activeFlag = 'N'
			List<GroupInfo> inactiveRelated = groups.stream()
					.filter(e -> inactiveRelatedIds.contains(e.getId()) && StringUtils.equals(e.getService(), service))
					.collect(Collectors.toList());

			// 合併兩者
			filtered.addAll(inactiveRelated);
			return BaseDataTransformer.transformData(filtered, UserGroupQueried.class);
		} else {
			throw new ValidationException("VALIDATION_FAILED", "該群組 ID 有誤，查詢失敗");
		}
	}

	/**
	 * 將使用者加入特定群組
	 * 
	 * @param command
	 */
	public void update(UpdateUserGroupsCommand command) {
		UserInfo userInfo = userInfoRepository.findByUsername(command.getUsername());

		// 要被更新的 Group 資料
		List<GroupInfo> groups = groupInfoRepository.findByIdInAndActiveFlag(command.getGroupIds(), YesNo.Y);

		// 取出非該服務的 Group
		List<GroupInfo> otherGroups = groupInfoRepository.findByServiceNot(command.getService());
		
		// 將群組資料轉為 UserGroup
		List<UserGroup> userGroups = this.processUpdatedGroupData(userInfo, groups, otherGroups);
		System.out.println("userGroups:"+userGroups);
		
		// 更新群組資料
		userInfo.updateGroups(userGroups);
		userInfoRepository.save(userInfo);
	}

	/**
	 * 處理要被更新的 Group 資料
	 * 
	 * @param userInfo
	 * @param groups
	 * @param otherGroups
	 */
	private List<UserGroup> processUpdatedGroupData(UserInfo userInfo, List<GroupInfo> groups,
			List<GroupInfo> otherGroups) {
		List<GroupInfo> groupList = new ArrayList<>();
		groupList.addAll(groups);
		groupList.addAll(otherGroups);
		System.out.println(groupList);
		// 將角色資料轉為 UserRole
		return groupList.stream().map(group -> {
			UserGroup userGroup = new UserGroup();
			userGroup.create(userInfo.getId(), group.getId());
			return userGroup;
		}).collect(Collectors.toList());
	}
}
