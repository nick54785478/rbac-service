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

import com.example.demo.domain.user.command.UpdateUserRolesCommand;
import com.example.demo.iface.dto.UpdateUserRolesResource;
import com.example.demo.iface.dto.UserRoleQueriedResource;
import com.example.demo.iface.dto.UserRolesUpdatedResource;
import com.example.demo.service.UserRoleCommandService;
import com.example.demo.service.UserRoleQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/roles")
public class UserRoleController {

	private UserRoleQueryService userRoleQueryService;
	private UserRoleCommandService userRoleCommandService;
	
	/**
	 * 更新使用者角色權限
	 * 
	 * @param resource
	 * @return ResponseEntity<UserRolesUpdatedResource>
	 */
	@PostMapping("/update")
	public ResponseEntity<UserRolesUpdatedResource> grant(@RequestBody UpdateUserRolesResource resource) {
		// 防腐處理 resource -> command
		UpdateUserRolesCommand command = BaseDataTransformer.transformData(resource, UpdateUserRolesCommand.class);
		userRoleCommandService.update(command);
		return new ResponseEntity<>(
				new UserRolesUpdatedResource("200", "成功更新使用者角色權限"),
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
	public ResponseEntity<List<UserRoleQueriedResource>> queryOthers(@PathVariable String usernmae) {
		return new ResponseEntity<>(BaseDataTransformer.transformData(userRoleQueryService.queryOthers(usernmae),
				UserRoleQueriedResource.class), HttpStatus.OK);
	}

}
