package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.entity.Category;
import com.shopme.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired
	CategoryReposiroty categoryReposiroty;

	public List<Category> listNoChildrenCategories() {
		List<Category> listNoChildrenCategories = new ArrayList<>();

		List<Category> listEnabledCategories = categoryReposiroty.findAllEnabled();

		listEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrenCategories.add(category);
			}
		});

		return listNoChildrenCategories;
	}

	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = categoryReposiroty.findByAliasEnabled(alias);
		if (category == null) {
			throw new CategoryNotFoundException("Could not found any categories with alias " + alias);
		}
		
		return category;
		
	}

	public List<Category> getCategoryParent(Category child) {
		List<Category> listParent = new ArrayList<>();
		
		Category parent = child.getParent();
		
		while(parent != null) {
			listParent.add(0, parent);
			parent = parent.getParent();
		}
		
		listParent.add(child);
		
		return listParent;
	}
}
