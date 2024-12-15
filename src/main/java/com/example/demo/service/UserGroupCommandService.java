package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.service.UserGroupService;
import com.example.demo.domain.user.command.UpdateUserGroupsCommand;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserGroupCommandService {

	private UserGroupService userGroupService;

	/**
	 * 將使用者們加入特定群組
	 * 
	 * @param command
	 * @return UserGroupAdded
	 */
	public void update(UpdateUserGroupsCommand command) {
		userGroupService.update(command);
	}
}
