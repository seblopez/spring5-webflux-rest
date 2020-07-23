package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;

public class VendorControllerTest {

    @Mock
    private VendorRepository vendorRepository;

    private VendorController vendorController;

    private WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getAll() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder()
                                .id("2323439mere431")
                                .firstName("Mike")
                                .lastName("Wazowski")
                                .build(),
                        Vendor.builder()
                                .id("23hjksh324wew")
                                .firstName("Sullivan")
                                .lastName("Mayfield")
                                .build())
                );

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(
                        Vendor.builder()
                                .id("2323439mere431")
                                .firstName("Mike")
                                .lastName("Wazowski")
                                .build()));

        webTestClient.get()
                .uri("/api/v1/vendors/2323439mere431")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class);
    }
}
