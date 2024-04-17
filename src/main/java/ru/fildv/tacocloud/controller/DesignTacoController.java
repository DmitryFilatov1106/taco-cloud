package ru.fildv.tacocloud.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.fildv.tacocloud.model.Ingredient;
import ru.fildv.tacocloud.model.Ingredient.Type;
import ru.fildv.tacocloud.model.Taco;
import ru.fildv.tacocloud.model.TacoOrder;
import ru.fildv.tacocloud.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.fildv.tacocloud.model.Ingredient.Type.values;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;

    @ModelAttribute
    public void addIngredientsToModel(final Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        for (Type type : values()) {
            model.addAttribute(
                    type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(
            final @Valid Taco taco,
            final Errors errors,
            final @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("processing taco: {}", taco);
        return "redirect:/orders/current";
    }

    private Iterable<Ingredient> filterByType(
            final Iterable<Ingredient> ingredients, final Type type) {
        List<Ingredient> list = new ArrayList<>();
        ingredients.forEach(it -> {
            if (it.getType().equals(type)) {
                list.add(it);
            }
        });
        return list;
    }
}
