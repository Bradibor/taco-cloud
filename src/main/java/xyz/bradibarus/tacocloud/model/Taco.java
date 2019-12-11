package xyz.bradibarus.tacocloud.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {
    @Id
    @GeneratedValue
    private Long id;
    private Date createdAt;
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    @NotNull
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany(targetEntity = Ingredient.class)
    private List<Ingredient> ingredients = Collections.emptyList();

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
