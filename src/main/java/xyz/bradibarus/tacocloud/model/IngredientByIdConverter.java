package xyz.bradibarus.tacocloud.model;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IngredientByIdConverter  implements Converter<String, Ingredient> {
    private IngredientRepository ingredientRepository;

    @Override
    public Ingredient convert(String s) {
        return ingredientRepository.findOne(s);
    }
}
