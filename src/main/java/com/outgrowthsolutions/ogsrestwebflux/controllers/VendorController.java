package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Vendor;
import com.outgrowthsolutions.ogsrestwebflux.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(VendorController.VENDORS_BASE_URL)
@RestController
public class VendorController {
    public static final String VENDORS_BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> getAllVendors(){
        return vendorRepository.findAll();
    }

    @GetMapping("/{vendorId}")
    public Mono<Vendor> getVendorById(@PathVariable String vendorId){
        return vendorRepository.findById(vendorId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();

    }
    @PutMapping("/{vendorId}")
    public Mono<Vendor> updateVendor(@PathVariable String vendorId, @RequestBody Vendor vendor){
        return vendorRepository.save(vendor);
    }
    @PatchMapping("/{vendorId}")
    public Mono<Vendor> patchVendor(@PathVariable String vendorId, @RequestBody Vendor vendor){

//        Mono<Vendor> foundVendor = vendorRepository.findById(vendorId);
//        return foundVendor
//                .filter(vendor1 -> !vendor1.getFirstName().equalsIgnoreCase(vendor.getFirstName()))
//                .then(vendorRepository.save(vendor));

        Vendor foundVendor = vendorRepository.findById(vendorId).block();

        if(!foundVendor.getFirstName().equals(vendor.getFirstName())){
            foundVendor.setFirstName(vendor.getFirstName());
            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
}
