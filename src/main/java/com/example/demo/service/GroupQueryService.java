package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.group.aggregate.GroupInfo;
import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.share.GroupInfoQueried;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupQueryService {

	private GroupInfoRepository groupInfoRepository;
	private GroupService groupService;

	/**
	 * 查詢符合條件的群組資料
	 * 
	 * @param service    服務
	 * @param type       群組種類
	 * @param name       群組名稱
	 * @param activeFlag 是否生效
	 * @return List<GroupInfoQueried>
	 */
	@Transactional
	public List<GroupInfoQueried> query(String service, String type, String name, String activeFlag) {
		List<GroupInfo> groups = groupInfoRepository.findAllWithSpecification(service, type, name, activeFlag);
		return BaseDataTransformer.transformData(groups, GroupInfoQueried.class);
	}

	/**
	 * 透過 ID 查詢群組資料
	 * 
	 * @param id
	 * @return List<GroupInfoQueried>
	 */
	@Transactional
	public GroupInfoQueried query(Long id) {
		return groupService.query(id);
	}
}
