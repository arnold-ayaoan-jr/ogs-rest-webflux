package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import com.outgrowthsolutions.ogsrestwebflux.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryController categoryController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getAllCategories() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Fruit").build(),
                                Category.builder().description("Exotic").build()));

        webTestClient.get().uri(CategoryController.CATEGORY_BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {
        String categoryId = "xxx";

        given(categoryRepository.findById(categoryId))
                .willReturn(Mono.just(Category.builder().description("Fruit").build()));

        webTestClient.get().uri(CategoryController.CATEGORY_BASE_URL+"/"+categoryId)
                .exchange()
                .expectBody(Category.class);
    }
}