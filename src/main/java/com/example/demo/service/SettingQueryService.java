package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.SettingService;
import com.example.demo.domain.share.SettingQueried;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SettingQueryService {

	private SettingService service;

	/**
	 * 根據條件查詢 Setting
	 * 
	 * @param dataType
	 * @param type
	 * @param name
	 * @param activeFlag
	 * @return List<SettingQueried> 設定清單
	 */
	public List<SettingQueried> query(String dataType, String type, String name, String activeFlag) {
		return service.query(dataType, type, name, activeFlag);
	}

}
