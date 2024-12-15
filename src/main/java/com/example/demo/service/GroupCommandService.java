package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.group.command.CreateGroupCommand;
import com.example.demo.domain.group.command.CreateOrUpdateGroupCommand;
import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.share.GroupCreated;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class GroupCommandService {

	private GroupService groupService;

	/**
	 * 建立一筆群組資料
	 * 
	 * @param command
	 * @return GroupCreated
	 */
	public GroupCreated create(CreateGroupCommand command) {
		return groupService.create(command);
	}
	
	/**
	 * 建立多筆群組資料
	 * 
	 * @param commands
	 */
	public void createOrUpdate(List<CreateOrUpdateGroupCommand> commands) {
		groupService.createOrUpdate(commands);
	}
	
	/**
	 * 刪除多筆群組資料
	 * 
	 * @param ids  要被刪除的 id 清單
	 * */
	public void delete(List<Long> ids) {
		groupService.delete(ids);
	}

	
	
}
