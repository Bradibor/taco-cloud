package xyz.bradibarus.tacocloud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import xyz.bradibarus.tacocloud.model.*;
import xyz.bradibarus.tacocloud.model.Ingredient.Type;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Controller
@RequestMapping("/design")
@RequiredArgsConstructor
@SessionAttributes({"order"})
public class DesignTacoController {
    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;

    @ModelAttribute("order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute("design")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute
    public void initIngredients(Model model) {
        Map<Type, List<Ingredient>> ingredientsByType = StreamSupport.stream(ingredientRepository.findAll().spliterator(), false)
                .collect(groupingBy(Ingredient::getType, Collectors.toList()));
        Arrays.stream(Type.values())
                .forEach(type-> model.addAttribute(type.toString().toLowerCase(), ingredientsByType.getOrDefault(type, Collections.emptyList())));
    }

    @GetMapping
    public String showDesignForm(Model model) {
//        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String design(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }
}
