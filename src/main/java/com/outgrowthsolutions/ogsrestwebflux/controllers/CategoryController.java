package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import com.outgrowthsolutions.ogsrestwebflux.repositories.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(CategoryController.CATEGORY_BASE_URL)
@RestController
public class CategoryController {
    public static final String CATEGORY_BASE_URL = "/api/v1/categories";
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping
    public Flux<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    @GetMapping("/{categoryId}")
    public Mono<Category> getCategoryById(@PathVariable String categoryId){
    return categoryRepository.findById(categoryId);
    }
}
