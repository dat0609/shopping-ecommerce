package com.shopme.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.entity.Currency;
import com.shopme.entity.setting.Setting;
import com.shopme.entity.setting.SettingCategory;

@Service
public class SettingService {

	@Autowired
	SettingRepository settingRepository;
	
	@Autowired
	CurrencyRepository currencyRepository;

	public List<Setting> getGeneralSetting() {
		return settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	public EmailSettingBag getEmailSettingBag() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}

	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(settings);
	}

	public PaymentSettingBag getPaymentSetting() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(settings);
	}
	
	public String getCurrencyCode() {
		Setting setting = settingRepository.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepository.findById(currencyId).get();
		
		return currency.getCode();
	}
}
