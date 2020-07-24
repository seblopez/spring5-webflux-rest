package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
        given(vendorRepository.findAll())
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
        given(vendorRepository.findById(anyString()))
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

    @Test
    public void createVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        final Vendor vendorToCreate = Vendor.builder()
                .firstName("Some")
                .lastName("Name")
                .build();

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(Flux.just(vendorToCreate), Vendor.class)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    public void updateVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Fabio").lastName("Posca").build());

        webTestClient.put()
                .uri("/api/v1/vendors/2343sfs3")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void patchVendorChangedLastNameOk() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder()
                        .id("2343sfs3")
                        .firstName("Favio")
                        .lastName("Posca")
                        .build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("Fabio")
                        .lastName("Posca")
                        .build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Fabio").lastName("Posca").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/2343sfs3")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).save(any(Vendor.class));
        verify(vendorRepository).findById(anyString());

    }

    @Test
    public void patchVendorChangedFirstNameOk() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder()
                        .id("2343sfs3")
                        .firstName("Favio")
                        .lastName("Posca")
                        .build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .id("2343sfs3")
                        .firstName("Fabio")
                        .lastName("Posca")
                        .build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Fabio").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/2343sfs3")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).save(any(Vendor.class));
        verify(vendorRepository).findById(anyString());

    }

    @Test
    public void patchVendorChangedFirstAndLastNameOk() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder()
                        .id("2343sfs3")
                        .firstName("Favio")
                        .lastName("Posa")
                        .build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("Fabio")
                        .lastName("Posca")
                        .build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Fabio").lastName("Posca").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/2343sfs3")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).save(any(Vendor.class));
        verify(vendorRepository).findById(anyString());

    }

    @Test
    public void patchVendorNoChangesOk() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder()
                        .id("2343sfs3")
                        .firstName("Fabio")
                        .lastName("Posca")
                        .build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("Fabio")
                        .lastName("Posca")
                        .build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().lastName("Posca").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/2343sfs3")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, never()).save(any(Vendor.class));
        verify(vendorRepository).findById(anyString());

    }

}
