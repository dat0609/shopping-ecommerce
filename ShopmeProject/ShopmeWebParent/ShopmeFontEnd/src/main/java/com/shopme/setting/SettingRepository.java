package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.setting.Setting;
import com.shopme.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, Integer>{

	public List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	public List<Setting> findByTwoCategory(SettingCategory category1, SettingCategory category2);
	
	public Setting findByKey(String key);
}
