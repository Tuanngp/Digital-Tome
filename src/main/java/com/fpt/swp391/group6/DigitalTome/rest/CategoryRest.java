package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.CategoryDto;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryRest {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/categories")
    public List<CategoryDto> getCategories(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return categoryService.searchCategories(search);
        } else {
            return categoryService.getAllCategories();
        }
    }
}
