package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.constant.YesNo;
import com.example.demo.domain.share.GroupOptionQueried;
import com.example.demo.domain.share.OptionQueried;
import com.example.demo.domain.share.RoleOptionQueried;
import com.example.demo.domain.share.UserOptionQueried;
import com.example.demo.infra.repository.GroupInfoRepository;
import com.example.demo.infra.repository.RoleInfoRepository;
import com.example.demo.infra.repository.SettingRepository;
import com.example.demo.infra.repository.UserInfoRepository;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionQueryService {

	private RoleInfoRepository roleInfoRepository;
	private SettingRepository settingRepository;
	private UserInfoRepository userInfoRepository;
	private GroupInfoRepository groupInfoRepository;

	/**
	 * 查詢相關的設定
	 * 
	 * @param service
	 * @param type 設定種類
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getSettingTypes(String service, String type) {
		return settingRepository.findByServiceAndDataTypeAndActiveFlag(service, type, YesNo.Y).stream().map(setting -> {
			return new OptionQueried(setting.getId(), setting.getType(), setting.getType());
		}).collect(Collectors.toList());
	}

	/**
	 * 查詢使用者資料 (AutoComplete)
	 * 
	 * @param str 使用者字串
	 * @return List<UserOptionQueried>
	 */
	public List<UserOptionQueried> getUserOptions(String str) {
		return BaseDataTransformer.transformData(userInfoRepository.findAllWithSpecification(str),
				UserOptionQueried.class);

	}

	/**
	 * 查詢角色資料 (AutoComplete)
	 * 
	 * @param str 角色字串
	 * @return List<RoleOptionQueried>
	 */
	public List<RoleOptionQueried> getRoleOptions(String str) {
		return BaseDataTransformer.transformData(roleInfoRepository.findAllWithSpecification(str),
				RoleOptionQueried.class);
	}

	/**
	 * 查詢群組資料 (AutoComplete)
	 * 
	 * @param str 群組字串
	 * @return List<GroupOptionQueried>
	 */
	public List<GroupOptionQueried> getGroupOptions(String str) {
		return BaseDataTransformer.transformData(groupInfoRepository.findAllWithSpecification(str),
				GroupOptionQueried.class);
	}
	
}
