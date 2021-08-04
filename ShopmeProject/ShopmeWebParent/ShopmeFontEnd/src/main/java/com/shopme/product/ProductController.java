package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.entity.Category;
import com.shopme.entity.Product;
import com.shopme.exception.CategoryNotFoundException;
import com.shopme.exception.ProductNotFoundException;

@Controller
public class ProductController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewCategoryByPage(alias, model, 1);
	}

	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model,
			@PathVariable("pageNum") int pageNum) {
		try {
			Category category = categoryService.getCategory(alias);

			List<Category> categoryParents = categoryService.getCategoryParent(category);

			Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProduct.getContent();

			long totalItem = pageProduct.getTotalElements();
			int totalPage = pageProduct.getTotalPages();

			model.addAttribute("category", category);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("totalItem", totalItem);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("categoryParents", categoryParents);

			return "products/products_by_category";
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias")String alias, Model model) {
		try {
			Product product = productService.getProduct(alias);
			List<Category> categoryParents = categoryService.getCategoryParent(product.getCategory());
			
			model.addAttribute("categoryParents", categoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());

			return "products/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
}
