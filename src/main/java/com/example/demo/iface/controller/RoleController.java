package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.role.command.CreateOrUpdateRoleCommand;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;
import com.example.demo.iface.dto.CreateOrUpdateRoleResource;
import com.example.demo.iface.dto.CreateRoleResource;
import com.example.demo.iface.dto.RoleCreatedOrUpdatedResource;
import com.example.demo.iface.dto.RoleCreatedResource;
import com.example.demo.iface.dto.RoleDeletedResource;
import com.example.demo.iface.dto.RoleInfoQueriedResource;
import com.example.demo.iface.dto.RoleUpdatedResource;
import com.example.demo.iface.dto.UpdateRoleResource;
import com.example.demo.service.RoleCommandService;
import com.example.demo.service.RoleQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

	private RoleQueryService roleQueryService;
	private RoleCommandService roleCommandService;


	/**
	 * 新增/更新多筆角色資料
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleCreatedResource>
	 */
	@PostMapping("/saveList")
	public ResponseEntity<RoleCreatedOrUpdatedResource> save(@RequestBody List<CreateOrUpdateRoleResource> resources) {
		// 防腐處理 resource -> command
		List<CreateOrUpdateRoleCommand> commands = BaseDataTransformer.transformData(resources,
				CreateOrUpdateRoleCommand.class);
		roleCommandService.createOrUpdate(commands);
		return new ResponseEntity<>(new RoleCreatedOrUpdatedResource("200", "新增/更新多筆角色資料"), HttpStatus.OK);
	}


	/**
	 * 刪除多筆角色資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 * @return ResponseEntity<RoleDeletedResource>
	 */
	@DeleteMapping("")
	public ResponseEntity<RoleDeletedResource> delete(@RequestBody List<Long> ids) {
		roleCommandService.delete(ids);
		return new ResponseEntity<>(new RoleDeletedResource("200", "成功刪除多筆角色資料"), HttpStatus.OK);
	}

	/**
	 * 查詢角色資料
	 * 
	 * @param service
	 * @param type
	 * @param name
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/query")
	public ResponseEntity<List<RoleInfoQueriedResource>> query(@RequestParam(required = false) String type,
			@RequestParam(required = true) String service, @RequestParam(required = false) String name,
			@RequestParam(required = false) String activeFlag) {
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(roleQueryService.query(service, type, name, activeFlag),
						RoleInfoQueriedResource.class),
				HttpStatus.OK);
	}

}
