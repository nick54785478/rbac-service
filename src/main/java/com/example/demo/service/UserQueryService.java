package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.UserInfoDetailsQueried;
import com.example.demo.domain.share.UserInfoQueried;
import com.example.demo.domain.share.UserInfoSummaryQueried;
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
	public UserInfoDetailsQueried getUserDetails(String username, String service) {
		return userService.getUserDetails(username, service);
	}

	/**
	 * 取得使用所有資訊(跨服務)
	 * 
	 * @param username
	 * @return UserInfoSummaryQueried
	 */
	public UserInfoSummaryQueried getUserSummary(String username) {
		return userService.getUserSummary(username);
	}

}
