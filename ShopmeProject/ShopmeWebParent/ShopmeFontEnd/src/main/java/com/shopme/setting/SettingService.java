package com.shopme.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.entity.Setting;
import com.shopme.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	SettingRepository settingRepository;
	
	public List<Setting> getGeneralSetting() {
		
		return settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
}