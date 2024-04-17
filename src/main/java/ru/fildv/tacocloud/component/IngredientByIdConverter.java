package ru.fildv.tacocloud.component;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.fildv.tacocloud.model.Ingredient;
import ru.fildv.tacocloud.repository.IngredientRepository;

@RequiredArgsConstructor
@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private final IngredientRepository ingredientRepo;

    @Override
    public Ingredient convert(final String id) {
        return ingredientRepo.findById(id).orElse(null);
    }
}
