package com.example.demo.iface.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.group.command.CreateGroupCommand;
import com.example.demo.domain.group.command.CreateOrUpdateGroupCommand;
import com.example.demo.domain.share.GroupInfoQueried;
import com.example.demo.iface.dto.CreateGroupResource;
import com.example.demo.iface.dto.CreateOrUpdateGroupResource;
import com.example.demo.iface.dto.GroupCreatedOrUpdatedResource;
import com.example.demo.iface.dto.GroupCreatedResource;
import com.example.demo.iface.dto.GroupDeletedResource;
import com.example.demo.iface.dto.GroupInfoQueriedResource;
import com.example.demo.iface.dto.PageResource;
import com.example.demo.iface.dto.PageableResource;
import com.example.demo.service.GroupCommandService;
import com.example.demo.service.GroupQueryService;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

	private GroupQueryService groupQueryService;
	private GroupCommandService groupCommandService;

	/**
	 * 新增 群組資料
	 * 
	 * @param resource
	 * @return ResponseEntity<GroupCreatedResource>
	 */
	@PostMapping("")
	public ResponseEntity<GroupCreatedResource> create(@RequestBody CreateGroupResource resource) {
		// 防腐處理 resource -> command
		CreateGroupCommand command = BaseDataTransformer.transformData(resource, CreateGroupCommand.class);
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(groupCommandService.create(command), GroupCreatedResource.class),
				HttpStatus.OK);
	}

	/**
	 * 新增/更新多筆群組資料
	 * 
	 * @param resource
	 * @return ResponseEntity<RoleCreatedResource>
	 */
	@PostMapping("/saveList")
	public ResponseEntity<GroupCreatedOrUpdatedResource> create(
			@RequestBody List<CreateOrUpdateGroupResource> resources) {
		// 防腐處理 resource -> command
		List<CreateOrUpdateGroupCommand> commands = BaseDataTransformer.transformData(resources,
				CreateOrUpdateGroupCommand.class);
		groupCommandService.createOrUpdate(commands);
		return new ResponseEntity<>(new GroupCreatedOrUpdatedResource("200", "新增/更新多筆角色資料"), HttpStatus.OK);
	}

	/**
	 * 查詢群組資料
	 * 
	 * @param type
	 * @param name
	 * @param actionFlag
	 * @param numberOfRows 筆數
	 * @param pageNumber   頁碼
	 * @return ResponseEntity<List<RoleQueriedResource>>
	 */
	@GetMapping("/query")
	public ResponseEntity<PageableResource<GroupInfoQueriedResource>> query(@RequestParam(required = false) String type,
			@RequestParam(required = false) String name, @RequestParam(required = false) String activeFlag,
			@RequestParam(value = "numberOfRows", defaultValue = "10") String numberOfRows,
			@RequestParam(value = "pageNumber", required = true, defaultValue = "0") String pageNumber) {

		Page<GroupInfoQueried> query = groupQueryService.query(type, name, activeFlag, Integer.valueOf(numberOfRows),
				Integer.valueOf(pageNumber));
		Page<GroupInfoQueriedResource> pageResult = BaseDataTransformer.transformData(query,
				GroupInfoQueriedResource.class);

		// DTO 處理
		PageableResource<GroupInfoQueriedResource> resource = new PageableResource<>();
		PageResource pageResource = new PageResource();
		pageResource.setNumber(pageResult.getNumber());	// 當前頁碼（從 0 開始）
		pageResource.setNumberOfElements(pageResult.getNumberOfElements()); // 當前頁實際返回的數據數量
		pageResource.setFirst(pageResult.getPageable().getPageNumber() == 0); // 判斷是否為第一頁
		pageResource.setEmpty(pageResult.getSize() == 0); // 判斷當前頁面資料是否為空
		pageResource.setSize(pageResult.getSize());	// 當前頁面數據大小
		pageResource.setTotalPages(pageResult.getTotalPages()); // 總頁數
		pageResource.setTotalElements(pageResult.getTotalElements());
		resource.setPage(pageResource);
		resource.setContent(pageResult.getContent());

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	/**
	 * 透過 ID 查詢角色資料
	 * 
	 * @param type
	 * @param name
	 * @return ResponseEntity<GroupInfoQueriedResource>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<GroupInfoQueriedResource> query(@PathVariable Long id) {
		return new ResponseEntity<>(
				BaseDataTransformer.transformData(groupQueryService.query(id), GroupInfoQueriedResource.class),
				HttpStatus.OK);
	}

	/**
	 * 刪除多筆群組資料
	 * 
	 * @param ids 要被刪除的 id 清單
	 * @return ResponseEntity<RoleDeletedResource>
	 */
	@DeleteMapping("")
	public ResponseEntity<GroupDeletedResource> delete(@RequestBody List<Long> ids) {
		groupCommandService.delete(ids);
		return new ResponseEntity<>(new GroupDeletedResource("200", "成功刪除多筆群組資料"), HttpStatus.OK);
	}

}
