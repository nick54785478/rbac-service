package com.example.demo.service;

import java.util.List;

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
	 * @return List<GroupInfoQueried>
	 */
	public List<GroupInfoQueried> query(String type, String name, String activeFlag) {
		return groupService.query(type, name, activeFlag);
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
