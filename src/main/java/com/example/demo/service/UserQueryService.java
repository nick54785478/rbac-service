package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.UserGroupQueried;
import com.example.demo.domain.share.UserInfoDetailQueried;
import com.example.demo.domain.share.UserInfoQueried;
import com.example.demo.domain.share.UserRoleQueried;
import com.example.demo.domain.user.aggregate.UserInfo;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserQueryService {

	private UserService userService;
	private UserInfoRepository userRepository;

	/**
	 * 取得特定使用者所在的 Group 資料
	 * 
	 * @param username 使用者帳號
	 * @return List<UserGroupQueried> 使用者群組清單
	 */
	public List<UserGroupQueried> queryGroups(String username) {
		List<UserGroupQueried> groups = userService.queryGroups(username);
		return groups;
	}

	/**
	 * 取得特定使用者所在的 Role 資料
	 * 
	 * @param username 使用者帳號
	 * @param service  服務
	 * @return List<UserRoleQueried>
	 */
	public List<UserRoleQueried> queryRoles(String username, String service) {
		List<UserRoleQueried> roles = userService.queryRoles(username);
		log.debug("roles: {}", roles);
		return roles;
	}

	/**
	 * 查詢該使用者資訊
	 * 
	 * @param username 使用者帳號
	 * @return UserInfoQueried
	 */
	@Transactional
	public UserInfoQueried query(String username) {
		UserInfo userInfo = userRepository.findByUsername(username);
		return BaseDataTransformer.transformData(userInfo, UserInfoQueried.class);
	}

	/**
	 * 取得使用者詳細資訊
	 * 
	 * @param username
	 * @param service
	 * @return UserInfoQueried
	 */
	public UserInfoDetailQueried getUserDetails(String username, String service) {
		return userService.getUserDetails(username, service);
	}

}
