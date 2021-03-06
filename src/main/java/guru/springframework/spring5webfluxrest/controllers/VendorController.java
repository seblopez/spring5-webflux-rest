package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    Flux<Vendor> getAll() {
        return vendorRepository.findAll();
    };

    @GetMapping("/{id}")
    Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        final Vendor vendorFound = vendorRepository.findById(id).block();

        final String firstName = vendor.getFirstName();
        final String lastName = vendor.getLastName();

        boolean changesMade = false;
        if(firstName != null && !vendorFound.getFirstName().equals(firstName)) {
            vendorFound.setFirstName(vendor.getFirstName());
            changesMade = true;
        }
        if(lastName != null && !vendorFound.getLastName().equals(lastName)) {
            vendorFound.setLastName(vendor.getLastName());
            changesMade = true;
        }

        return changesMade ? vendorRepository.save(vendor) : Mono.just(vendorFound);

    }

}
