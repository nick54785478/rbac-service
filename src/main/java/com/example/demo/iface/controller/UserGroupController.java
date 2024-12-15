package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.user.command.UpdateUserGroupsCommand;
import com.example.demo.iface.dto.UpdateUserGroupsResource;
import com.example.demo.iface.dto.UserGroupQueriedResource;
import com.example.demo.iface.dto.UserGroupUpdatedResource;
import com.example.demo.service.UserGroupCommandService;
import com.example.demo.service.UserGroupQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/groups")
public class UserGroupController {

	private UserGroupQueryService userGroupQueryService;
	private UserGroupCommandService userGroupCommandService;
	
	/**
	 * 將使用者加入特定群組
	 * 
	 * @param resource
	 * @return ResponseEntity<UserGroupUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<UserGroupUpdatedResource> update(@RequestBody UpdateUserGroupsResource resource) {
		// 防腐處理 resource -> command
		UpdateUserGroupsCommand command = BaseDataTransformer.transformData(resource, UpdateUserGroupsCommand.class);
		userGroupCommandService.update(command);
		return new ResponseEntity<>(
				new UserGroupUpdatedResource("200", "成功更新使用者群組權限"),
				HttpStatus.OK);
	}

	/**
	 * 查詢不屬於該使用者的群組資料
	 * 
	 * @param type
	 * @param name
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/{usernmae}/others")
	public ResponseEntity<List<UserGroupQueriedResource>> query(@PathVariable String usernmae) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(userGroupQueryService.queryOthers(usernmae),
				UserGroupQueriedResource.class), HttpStatus.OK);
	}

}
