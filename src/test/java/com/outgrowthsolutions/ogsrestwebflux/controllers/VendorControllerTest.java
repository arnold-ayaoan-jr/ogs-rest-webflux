package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Vendor;
import com.outgrowthsolutions.ogsrestwebflux.repositories.VendorRepository;
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
class VendorControllerTest {
    @Mock
    VendorRepository vendorRepository;
    @InjectMocks
    VendorController vendorController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getAllVendors() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Arnold").lastName("Ayaoan").build(),
                        Vendor.builder().firstName("Charmaine").lastName("Ayaoan").build()));

        webTestClient.get().uri(VendorController.VENDORS_BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        String vendorId = "abc";
        given(vendorRepository.findById(vendorId))
                .willReturn(Mono.just(Vendor.builder().firstName("Rinoa Elisha").lastName("Ayaoan").build()));

        webTestClient.get().uri(VendorController.VENDORS_BASE_URL + "/" + vendorId)
                .exchange()
                .expectBody(Vendor.class);
    }
}