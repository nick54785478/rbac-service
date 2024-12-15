package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.share.RoleInfoCreated;
import com.example.demo.domain.share.RoleInfoUpdated;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class RoleCommandService {

	private RoleService roleService;

	/**
	 * 建立角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public RoleInfoCreated create(CreateRoleCommand command) {
		return roleService.create(command);
	}

	/**
	 * 建立多筆角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public void createOrUpdate(List<CreateOrUpdateRoleCommand> commands) {
		roleService.createOrUpdate(commands);
	}

	/**
	 * 更新角色資料
	 * 
	 * @param command
	 * @return RoleInfoCreated
	 */
	public RoleInfoUpdated update(UpdateRoleCommand command) {
		return roleService.update(command);
	}

	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids  要被刪除的 id 清單
	 * */
	public void delete(List<Long> ids) {
		roleService.delete(ids);
	}

	
}
