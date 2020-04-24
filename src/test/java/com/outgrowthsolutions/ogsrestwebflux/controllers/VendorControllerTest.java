package com.outgrowthsolutions.ogsrestwebflux.controllers;

import com.outgrowthsolutions.ogsrestwebflux.domain.Vendor;
import com.outgrowthsolutions.ogsrestwebflux.repositories.VendorRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    @Test
    void createVendor() {
        Vendor rinoaVendor = Vendor.builder().firstName("Rinoa Elisha").lastName("Ayaoan").build();
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(rinoaVendor));

        webTestClient.post().uri(VendorController.VENDORS_BASE_URL)
                .body(Mono.just(rinoaVendor),Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void updateVendor() {
        Vendor rinoaVendor = Vendor.builder().firstName("Rinoa Elisha").lastName("Ayaoan").build();
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(rinoaVendor));

        webTestClient.put().uri(VendorController.VENDORS_BASE_URL+"/xxx")
                .body(Mono.just(rinoaVendor),Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void patchVendorModified() {
        Vendor rinoaVendor = Vendor.builder().firstName("Rinoa Elisha").lastName("Ayaoan").build();
        Vendor arnoldVendor = Vendor.builder().firstName("Arnold").lastName("Ayaoan").build();
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(arnoldVendor));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(rinoaVendor));

        webTestClient.patch().uri(VendorController.VENDORS_BASE_URL+"/xxx")
                .body(Mono.just(rinoaVendor),Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).findById(anyString());
        verify(vendorRepository).save(any(Vendor.class));
    }
    @Test
    void patchVendorNotModified() {
        Vendor arnoldVendor = Vendor.builder().id("xxx").firstName("Arnold").lastName("Ayaoan").build();
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(arnoldVendor));

        webTestClient.patch().uri(VendorController.VENDORS_BASE_URL+"/xxx")
                .body(Mono.just(arnoldVendor),Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).findById(anyString());
        verify(vendorRepository,never()).save(any());

//        given(vendorRepository.findById(anyString()))
//                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));
//
////        given(vendorRepository.save(any(Vendor.class)))
////                .willReturn(Mono.just(Vendor.builder().build()));
//
//        Mono<Vendor> vendorMonoToUpdate = Mono.just(Vendor.builder().firstName("Jimmy").build());
//
//        webTestClient.patch()
//                .uri(VendorController.VENDORS_BASE_URL+"/someid")
//                .body(vendorMonoToUpdate, Vendor.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        verify(vendorRepository, never()).save(any());

    }

}