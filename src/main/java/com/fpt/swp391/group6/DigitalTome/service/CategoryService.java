package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.CategoryDto;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public List<CategoryDto> searchCategories(String search) {
        return categoryRepository.findByNameContainingIgnoreCase(search).stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
    public Optional<CategoryEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }

}

