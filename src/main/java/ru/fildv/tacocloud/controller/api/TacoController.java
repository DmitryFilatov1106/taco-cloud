package ru.fildv.tacocloud.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.fildv.tacocloud.model.Taco;
import ru.fildv.tacocloud.repository.TacoRepository;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/tacos", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
public class TacoController {
    private final TacoRepository tacoRepo;

    @GetMapping(params = "recent")
    public Iterable<Taco> recentTacos() {
        PageRequest page = PageRequest.of(0, 12,
                Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(final @PathVariable("id") Long id) {
        Optional<Taco> oTaco = tacoRepo.findById(id);
        return oTaco.map(
                        taco -> new ResponseEntity<>(taco, HttpStatus.OK))
                .orElseGet(
                        () -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                );
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(final @RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Taco putTaco(
            final @PathVariable("id") Long id,
            final @RequestBody Taco taco) {
        taco.setId(id);
        return tacoRepo.save(taco);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Taco patchTaco(
            final @PathVariable("id") Long id,
            final @RequestBody Taco patch) {
        Optional<Taco> oTaco = tacoRepo.findById(id);
        if (oTaco.isPresent()) {
            Taco taco = oTaco.get();

            if (patch.getName() != null) {
                taco.setName(patch.getName());
            }

            if (patch.getCreatedAt() != null) {
                taco.setCreatedAt(patch.getCreatedAt());
            }

            return tacoRepo.save(taco);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletetaco(final @PathVariable("id") Long id) {
        try {
            tacoRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {

        }
    }
}
