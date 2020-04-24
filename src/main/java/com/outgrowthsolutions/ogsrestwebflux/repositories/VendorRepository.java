package com.outgrowthsolutions.ogsrestwebflux.repositories;

import com.outgrowthsolutions.ogsrestwebflux.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor,String> {
}
