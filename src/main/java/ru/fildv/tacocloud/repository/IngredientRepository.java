package ru.fildv.tacocloud.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fildv.tacocloud.model.Ingredient;

public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {
}
