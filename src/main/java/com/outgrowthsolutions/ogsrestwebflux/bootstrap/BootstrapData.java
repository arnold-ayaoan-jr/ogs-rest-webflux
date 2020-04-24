package com.outgrowthsolutions.ogsrestwebflux.bootstrap;

import com.outgrowthsolutions.ogsrestwebflux.domain.Category;
import com.outgrowthsolutions.ogsrestwebflux.domain.Vendor;
import com.outgrowthsolutions.ogsrestwebflux.repositories.CategoryRepository;
import com.outgrowthsolutions.ogsrestwebflux.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BootstrapData implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public BootstrapData(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data....");
        loadCategories();
        loadVendors();
        categoryRepository.count().subscribe(count -> log.info("Category count:{}", count));
        vendorRepository.count().subscribe(count -> log.info("Vendors count:{}", count));
        log.info("Loading data successful.");

    }

    private void loadVendors() {
//        vendorRepository.count().subscribe(count -> {
            if (vendorRepository.count().block() == 0) {
                vendorRepository.save(Vendor.builder().firstName("Arnold Jr").lastName("Ayaoan").build()).subscribe();
                vendorRepository.save(Vendor.builder().firstName("Charmaine").lastName("Ayaoan").build()).subscribe();
                vendorRepository.save(Vendor.builder().firstName("Rinoa Elisha").lastName("Ayaoan").build()).subscribe();
            }
//        });

    }

    private void loadCategories() {
//        categoryRepository.count().subscribe(count -> {
            if (categoryRepository.count().block() == 0) {
                categoryRepository.save(Category.builder().description("Fruit").build()).subscribe();
                categoryRepository.save(Category.builder().description("Dried").build()).subscribe();
                categoryRepository.save(Category.builder().description("Fresh").build()).subscribe();
                categoryRepository.save(Category.builder().description("Exotic").build()).subscribe();
                categoryRepository.save(Category.builder().description("Nuts").build()).subscribe();
            }
//        });

    }


}
