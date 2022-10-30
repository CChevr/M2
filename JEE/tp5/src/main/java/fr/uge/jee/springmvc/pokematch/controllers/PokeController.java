package fr.uge.jee.springmvc.pokematch.controllers;

import fr.uge.jee.springmvc.pokematch.models.Pokedex;
import fr.uge.jee.springmvc.pokematch.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@SessionAttributes("user")
public class PokeController {
    private Pokedex pokedex;

    @ModelAttribute("user")
    public User user(){
        return new User();
    }

    @Autowired
    public void setPokedex(Pokedex pokedex) {
        this.pokedex = Objects.requireNonNull(pokedex);
    }

    @GetMapping("/pokematch")
    public String greeting(Model model, @ModelAttribute("hi") User user) {
        return "pokematch";
    }

    @PostMapping("/pokematch")
    public String processForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "pokematch";
        }

        var id = user.getFirstName().hashCode() + user.getLastName().hashCode();
        var pokemon = pokedex.getPokemon(id);
        var image = pokedex.getPokemonImage(pokemon);

        if (image.isPresent()) {
            model.addAttribute("pokemon", image.get());
        } else {
            System.out.println("No image found");
        }

        return "pokematch";
    }
}
