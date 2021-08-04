package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.entity.Role;
import com.shopme.entity.User;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/users")
	public String listFirstPage(Model model) {

		return listByPage(1, model, null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("keyword") String keyword) {
		Page<User> page = userService.listByPage(pageNum, keyword);
		List<User> listUsers = page.getContent();

		long totalItem = page.getTotalElements();
		int totalPage = page.getTotalPages();

		model.addAttribute("pageNum", pageNum);
		model.addAttribute("totalItem", totalItem);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("keyword", keyword);

		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = userService.listRoles();
		User user = new User();

		model.addAttribute("listRoles", listRoles);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New User");

		return "users/users_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			
			String uploadDir = "./user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			userService.save(user);
		}
		
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully." );

		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes, Model model)
			throws UserNotFoundException {
		try {
			List<Role> listRoles = userService.listRoles();

			User user = userService.get(id);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User");
			model.addAttribute("listRoles", listRoles);

			return "users/users_form";

		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model)
			throws UserNotFoundException {

		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully" );

		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enable/{status}")
	public String updateEnable(@PathVariable("id") Integer id, @PathVariable("status") boolean enable,
			RedirectAttributes redirectAttributes) {

		userService.updateEnable(id, enable);
		String msg = enable ? "Enabled" : "Disable";

		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = userService.listAll();
		
		UserCsvExporter  exporter = new UserCsvExporter();
		
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = userService.listAll();
		
		UserExcelExporter  exporter = new UserExcelExporter();
		
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportPDF(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = userService.listAll();
		
		UserPdfExporter  exporter = new UserPdfExporter();
		
		exporter.export(listUsers, response);
	}
}
