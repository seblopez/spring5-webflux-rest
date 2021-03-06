package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
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
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

public class CategoryControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getListOk() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder()
                                .id("2323439mere431")
                                .description("Category 1")
                                .build(),
                        Category.builder()
                                .id("23hjksh324wew")
                                .description("Category 2")
                                .build())
                        );

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(
                        Category.builder()
                                .id("2323439mere431")
                                .description("Category 1")
                                .build()));
        webTestClient.get()
                .uri("/api/v1/categories/2323439mere431")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class);
    }

    @Test
    public void createCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Beans").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryToSaveMono, Category.class)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    public void updateCategory() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description("Beans").build());

        webTestClient.put()
                .uri("/api/v1/categories/2343sfs3")
                .body(categoryToUpdateMono, Category.class)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void patchCategoryWithChanges() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().description("Bean").build()));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description("Beans").build());

        webTestClient.patch()
                .uri("/api/v1/categories/2343sfs3")
                .body(categoryToUpdateMono, Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository).findById(anyString());

    }

    @Test
    public void patchCategoryWithNoChanges() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("Beans").build()));

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Beans").build()));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description("Beans").build());

        webTestClient.patch()
                .uri("/api/v1/categories/2343sfs3")
                .body(categoryToUpdateMono, Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryRepository).findById(anyString());

    }
}
