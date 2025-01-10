package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.share.GroupInfoQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupQueryService {

	private GroupService groupService;

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @param numberOfRows 資料筆數
	 * @param pageNumber 頁碼
	 * @return List<GroupInfoQueried>
	 */
	public Page<GroupInfoQueried> query(String type, String name, String activeFlag, Integer numberOfRows,
			Integer pageNumber) {
		return groupService.query(type, name, activeFlag, numberOfRows, pageNumber);
	}

	/**
	 * 透過 ID 查詢群組資料
	 * 
	 * @param id
	 * @return List<GroupInfoQueried>
	 */
	public GroupInfoQueried query(Long id) {
		return groupService.query(id);
	}
}
