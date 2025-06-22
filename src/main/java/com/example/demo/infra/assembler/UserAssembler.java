package com.example.demo.infra.assembler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.role.aggregate.RoleInfo;
import com.example.demo.domain.share.FunctionInfoDetailsQueried;
import com.example.demo.domain.share.UserGroupDetailsQueried;
import com.example.demo.domain.share.UserGroupSummaryQueried;
import com.example.demo.domain.share.UserInfoDetailsQueried;
import com.example.demo.domain.share.UserInfoSummaryQueried;
import com.example.demo.domain.share.UserRoleDetailsQueried;
import com.example.demo.domain.share.UserRoleSummaryQueried;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserAssembler {

	/**
	 * 組裝 UserInfoDetailQueried 資料
	 * 
	 * @param userInfo
	 * @param roleList
	 * @param groupList
	 * @param functions
	 * @return UserInfoDetailQueried
	 */
	public UserInfoDetailsQueried assembleUserInfoDetails(UserInfo userInfo, List<UserRoleDetailsQueried> roleList,
			List<UserGroupDetailsQueried> groupList, List<FunctionInfoDetailsQueried> functions) {
		UserInfoDetailsQueried result = BaseDataTransformer.transformData(userInfo, UserInfoDetailsQueried.class);
		result.setRoles(roleList);
		result.setGroups(groupList);
		result.setFunctions(functions);
		return result;
	}

	/**
	 * 組裝 UserInfoSummaryQueried 資料
	 * 
	 * @param userInfo
	 * @param roleList
	 * @param groupList
	 * @return UserInfoSummaryQueried
	 */
	public UserInfoSummaryQueried assemblerUserInfoSummary(UserInfo userInfo, List<RoleInfo> roleList, List<GroupInfo> groupList) {
		UserInfoSummaryQueried result = BaseDataTransformer.transformData(userInfo, UserInfoSummaryQueried.class);
		List<UserRoleSummaryQueried> roles = BaseDataTransformer.transformData(roleList, UserRoleSummaryQueried.class);
		List<UserGroupSummaryQueried> groups = BaseDataTransformer.transformData(groupList, UserGroupSummaryQueried.class);
		result.setRoles(roles);
		result.setGroups(groups);
		return result;
	}

}
