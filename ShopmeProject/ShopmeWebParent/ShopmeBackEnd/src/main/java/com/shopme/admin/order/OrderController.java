package com.shopme.admin.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.setting.SettingService;
import com.shopme.entity.order.Order;
import com.shopme.entity.setting.Setting;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	SettingService settingService;
	
	@GetMapping("/orders")
	public String listAll(Model model, HttpServletRequest request) {
		return listByPage(1, model,	null, request);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("keyword") String keyword,
			HttpServletRequest request) {
		
		Page<Order> page = orderService.listByPage(pageNum, keyword);
		List<Order> listOrders = page.getContent();
		

		long totalItem = page.getTotalElements();
		int totalPage = page.getTotalPages();
		loadCurrencySetting(request);

		model.addAttribute("pageNum", pageNum);
		model.addAttribute("totalItem", totalItem);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("keyword", keyword);

		return "orders/orders";
	}
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();

		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}	
	
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) {
		try {
			Order order = orderService.get(id);
			loadCurrencySetting(request);			
			model.addAttribute("order", order);

			return "orders/order_details_modal";
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return "orders/orders";
		}

	}
}
