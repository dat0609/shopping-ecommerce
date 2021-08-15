package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.setting.Setting;
import com.shopme.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, Integer>{

	public List<Setting> findByCategory(SettingCategory category);
}
