package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {

    final CategoryRepository categoryRepository;
    final VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadVendors();
    }

    private void loadCategories() {
        final Long documentCount = categoryRepository.count().block();
        if (documentCount == 0L) {
            List<Category> categories = Arrays.asList(
                    Category.builder()
                            .description("Fruit")
                            .build(),
                    Category.builder()
                            .description("Nuts")
                            .build(),
                    Category.builder()
                            .description("Breads")
                            .build(),
                    Category.builder()
                            .description("Meats")
                            .build(),
                    Category.builder()
                            .description("Eggs")
                            .build(),
                    Category.builder()
                            .description("Dried")
                            .build());
            categoryRepository.saveAll(categories).blockLast();
        }
        log.info(MessageFormat.format("Category document count {0}", categoryRepository.count().block().toString()));
    }

    private void loadVendors() {
        final Long documentCount = vendorRepository.count().block();
        if (documentCount == 0L) {
            final List<Vendor> vendors = Arrays.asList(
                    Vendor.builder()
                            .firstName("Gordon")
                            .lastName("Sumner")
                            .build(),
                    Vendor.builder()
                            .firstName("Peter")
                            .lastName("Tosh")
                            .build(),
                    Vendor.builder()
                            .firstName("Phil")
                            .lastName("Collins")
                            .build(),
                    Vendor.builder()
                            .firstName("Charly")
                            .lastName("Garcia")
                            .build(),
                    Vendor.builder()
                            .firstName("Bob")
                            .lastName("Marley")
                            .build(),
                    Vendor.builder()
                            .firstName("Gustavo")
                            .lastName("Cerati")
                            .build(),
                    Vendor.builder()
                            .firstName("Nile")
                            .lastName("Rodgers")
                            .build()
            );
            vendorRepository.saveAll(vendors).blockLast();
            log.info(MessageFormat.format("Vendor Document count {0}", vendorRepository.count().block().toString()));
        }
    }
}
