package com.example.demo.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.share.OptionQueried;
import com.example.demo.domain.share.enums.YesNo;
import com.example.demo.infra.repository.SettingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionService {

	private SettingRepository settingRepository;

	/**
	 * 查詢相關的設定 (dropdown)
	 * 
	 * @param type 設定種類
	 * @return List<OptionQueried>
	 */
	public List<OptionQueried> getSettingTypes(String dataType) {
		return settingRepository.findByDataTypeAndActiveFlag(dataType, YesNo.Y).stream().map(setting -> {
			return new OptionQueried(setting.getId(), setting.getType(), setting.getType());
		}).collect(Collectors.toList());
	}

}
