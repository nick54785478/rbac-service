package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.UserInfoUpdated;
import com.example.demo.domain.share.UserRolesGranted;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.exception.ValidationException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserCommandService {

	private UserService userService;

	/**
	 * 建立使用者資料
	 * 
	 * @param command
	 */
	public void create(CreateUserCommand command) {
		if (!userService.checkIsRegistered(command)) {
			throw new ValidationException("VALIDATION_FAILED", "該使用者相關資訊已註冊");
		}

		userService.create(command);
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 * @return UserInfoUpdated
	 */
	public UserInfoUpdated update(UpdateUserCommand command) {
		return userService.update(command);
	}

	/**
	 * 更新 使用者角色資料
	 * 
	 * @param command
	 * @return UserRolesGranted
	 */
	public UserRolesGranted grant(UpdateUserRolesCommand command) {
		return userService.grant(command);
	}
}
