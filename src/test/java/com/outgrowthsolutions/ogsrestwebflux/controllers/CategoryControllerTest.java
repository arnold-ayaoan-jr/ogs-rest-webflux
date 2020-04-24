package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import com.outgrowthsolutions.ogsrestwebflux.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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

    @Test
    void createCategory() {
        Category fruit = Category.builder().description("Fruit").build();

        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(fruit));

        webTestClient.post().uri(CategoryController.CATEGORY_BASE_URL)
                .body(Mono.just(fruit),Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void updateCategory() {
        Category fruit = Category.builder().description("Fruit").build();

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(fruit));

        webTestClient.put().uri(CategoryController.CATEGORY_BASE_URL+"/xxx")
                .body(Mono.just(fruit),Category.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void patchCategoryModified() {
        Category fruit = Category.builder().description("Fruit").build();
        Category tropicalFruit = Category.builder().description("Tropical Fruit").build();

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(fruit));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(tropicalFruit));

        webTestClient.patch().uri(CategoryController.CATEGORY_BASE_URL+"/xxx")
                .body(Mono.just(tropicalFruit),Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository).findById(anyString());
        verify(categoryRepository).save(any());
    }
    void patchCategoryNotModified() {
        Category fruit = Category.builder().description("Fruit").build();

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(fruit));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(fruit));

        webTestClient.patch().uri(CategoryController.CATEGORY_BASE_URL+"/xxx")
                .body(Mono.just(fruit),Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository).findById(anyString());
        verifyNoInteractions(categoryRepository.save(any()));
    }
}