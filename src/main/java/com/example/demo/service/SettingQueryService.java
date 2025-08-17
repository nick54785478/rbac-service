package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.SettingService;
import com.example.demo.domain.share.SettingQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SettingQueryService {

	private SettingService settingService;

	/**
	 * 根據條件查詢 Setting
	 * 
	 * @param service    服務
	 * @param dataType   資料種類
	 * @param type       設定類別
	 * @param name       名稱
	 * @param activeFlag 是否生效
	 * @return List<SettingQueried> 設定清單
	 */
	public List<SettingQueried> query(String service, String dataType, String type, String name, String activeFlag) {
		return settingService.query(service, dataType, type, name, activeFlag);
	}

}
