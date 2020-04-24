package com.outgrowthsolutions.ogsrestwebflux.repositories;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
