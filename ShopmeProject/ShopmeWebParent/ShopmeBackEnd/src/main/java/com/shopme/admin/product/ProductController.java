package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.entity.Brand;
import com.shopme.entity.Product;

@Controller
public class ProductController {
	@Autowired 
	private ProductService productService;
	
	@Autowired
	BrandService brandService;

	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();

		model.addAttribute("listProducts", listProducts);

		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");

		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam("fileImage") MultipartFile mainImage,
			@RequestParam("extraImage") MultipartFile[] extraImage,
			@RequestParam(name = "detailNames", required = false)String[] detailNames,
			@RequestParam(name = "detailValues", required = false)String[] detailVaules) throws IOException {

		setMainImage(mainImage, product);
		setExtraImageNames(extraImage, product);
		setProductDetails(detailNames, detailVaules, product);
		
		Product savedProduct = productService.save(product);
		
		saveUploadImages(mainImage, extraImage, savedProduct);
	
		ra.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
	
	private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0) return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];

			if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
	}

	private void saveUploadImages(MultipartFile mainImage, MultipartFile[] extraImage, Product savedProduct) throws IOException {
		if (!mainImage.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
			
			String uploadDir = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImage);
		}
		
		if (extraImage.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			for(MultipartFile multipartFile : extraImage) {
				if (multipartFile.isEmpty()) continue;
					
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}

	}

	private void setMainImage(MultipartFile mainImage, Product product) {
		if (!mainImage.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
	
	private void setExtraImageNames(MultipartFile[] extraImage, Product product) {
		if (extraImage.length > 0) {
			for(MultipartFile multipartFile : extraImage) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					product.addExtraImage(fileName);
				}
			}
		}
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model)
			throws ProductNotFoundException {

		try {
			productService.delete(id);
			String productExtraImageDir = "../product-images/" + id + "/extras";
			String productImageDir = "../product-images/" + id;
			FileUploadUtil.removeDir(productExtraImageDir);
			FileUploadUtil.removeDir(productImageDir);
			
			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully" );

		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}

}
