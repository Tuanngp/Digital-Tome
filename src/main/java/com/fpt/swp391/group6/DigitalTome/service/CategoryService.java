package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryEntity> searchCategories(String search) {
        return categoryRepository.findByNameContainingIgnoreCase(search);
    }
    public Optional<CategoryEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 728ce2091d5a52ed77fa453748e001245b19c9ed
