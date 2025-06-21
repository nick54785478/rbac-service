package com.example.demo.infra.assembler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.share.FunctionInfoDetailQueried;
import com.example.demo.domain.share.UserGroupDetailsQueried;
import com.example.demo.domain.share.UserInfoDetailQueried;
import com.example.demo.domain.share.UserRoleDetailsQueried;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserDetailsAssembler {

	/**
	 * 組裝 UserInfoDetailQueried 資料
	 * 
	 * @param userInfo
	 * @param roleList
	 * @param groupList
	 * @param functions
	 * @return UserInfoDetailQueried
	 */
	public UserInfoDetailQueried assembleUserInfoDetails(UserInfo userInfo, List<UserRoleDetailsQueried> roleList,
			List<UserGroupDetailsQueried> groupList, List<FunctionInfoDetailQueried> functions) {
		UserInfoDetailQueried resource = BaseDataTransformer.transformData(userInfo, UserInfoDetailQueried.class);
		resource.setRoles(roleList);
		resource.setGroups(groupList);
		resource.setFunctions(functions);
		return resource;
	}

}
