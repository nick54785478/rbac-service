package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.GroupService;
import com.example.demo.domain.service.OptionService;
import com.example.demo.domain.service.RoleService;
import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.GroupOptionQueried;
import com.example.demo.domain.share.OptionQueried;
import com.example.demo.domain.share.RoleOptionQueried;
import com.example.demo.domain.share.UserOptionQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionQueryService {

	private RoleService roleService;
	private UserService userService;
	private GroupService groupService;
	private OptionService optionService;

	/**
	 * 查詢相關的設定
	 * 
	 * @param type 設定種類
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getSettingTypes(String type) {
		return optionService.getSettingTypes(type);
	}

	/**
	 * 查詢使用者資料 (AutoComplete)
	 * 
	 * @param str 使用者字串
	 * @return List<UserOptionQueried>
	 */
	public List<UserOptionQueried> getUserOptions(String str) {
		return userService.getUserInfoOtions(str);
	}

	/**
	 * 查詢角色資料 (AutoComplete)
	 * 
	 * @param str 角色字串
	 * @return List<RoleOptionQueried>
	 */
	public List<RoleOptionQueried> getRoleOptions(String str) {
		return roleService.getRoleInfoOtions(str);
	}

	/**
	 * 查詢群組資料 (AutoComplete)
	 * 
	 * @param str 群組字串
	 * @return List<GroupOptionQueried>
	 */
	public List<GroupOptionQueried> getGroupOptions(String str) {
		return groupService.getGroupInfoOtions(str);
	}
}
