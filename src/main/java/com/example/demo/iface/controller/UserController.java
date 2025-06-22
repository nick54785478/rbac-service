package com.example.demo.iface.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.iface.dto.CreateUserResource;
import com.example.demo.iface.dto.UpdateUserResource;
import com.example.demo.iface.dto.UserCreatedResource;
import com.example.demo.iface.dto.UserInfoDetailQueriedResource;
import com.example.demo.iface.dto.UserUpdatedResource;
import com.example.demo.service.UserCommandService;
import com.example.demo.service.UserQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private UserQueryService userQueryService;
	private UserCommandService userCommandService;

	/**
	 * 註冊 使用者資料
	 * 
	 * @param resource
	 * @return ResponseEntity<UserCreatedResource>
	 */
	@PostMapping("/register")
	public ResponseEntity<UserCreatedResource> create(@RequestBody CreateUserResource resource) {
		// 防腐處理 resource -> command
		CreateUserCommand command = BaseDataTransformer.transformData(resource, CreateUserCommand.class);
		userCommandService.create(command);
		return new ResponseEntity<>(new UserCreatedResource("201", "註冊成功"), HttpStatus.CREATED);
	}

	/**
	 * 更新 使用者資料
	 * 
	 * @param resource
	 * @return ResponseEntity<UserUpdatedResource>
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserUpdatedResource> update(@RequestBody UpdateUserResource resource, @PathVariable Long id) {
		// 防腐處理 resource -> command
		UpdateUserCommand command = BaseDataTransformer.transformData(resource, UpdateUserCommand.class);
		command.setId(id);
		userCommandService.update(command);
		return new ResponseEntity<>(new UserUpdatedResource("200", "更新使用者資料成功"), HttpStatus.OK);
	}

	/**
	 * 查詢該使用者相關資訊(含權限、角色)
	 * 
	 * @param username
	 * @return ResponseEntity<UserInfoDetailQueriedResource>
	 */
	@GetMapping("/{username}/details")
	public ResponseEntity<UserInfoDetailQueriedResource> queryUserDetails(@PathVariable String username,
			@RequestParam String service) {
		return new ResponseEntity<>(BaseDataTransformer
				.transformData(userQueryService.getUserDetails(username, service), UserInfoDetailQueriedResource.class),
				HttpStatus.OK);
	}

}
