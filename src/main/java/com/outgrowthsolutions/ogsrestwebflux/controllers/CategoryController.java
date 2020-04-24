package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import com.outgrowthsolutions.ogsrestwebflux.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{categoryId}")
    public Mono<Category> getCategoryById(@PathVariable String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/{categoryId}")
    public Mono<Category> updateCategory(@PathVariable String categoryId, @RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PatchMapping("/{categoryId}")
    public Mono<Category> patchCategory(@PathVariable String categoryId, @RequestBody Category category) {
//        Mono<Category> foundCategory = categoryRepository.findById(categoryId);
//
//        return foundCategory
//                .filter(category1 -> !category1.getDescription().equalsIgnoreCase(category.getDescription()))
//                .then(categoryRepository.save(category));
        Category foundCategory = categoryRepository.findById(categoryId).block();
        if (!foundCategory.getDescription().equalsIgnoreCase(category.getDescription())) {
            return categoryRepository.save(category);
        }
        return Mono.just(foundCategory);
    }
}
