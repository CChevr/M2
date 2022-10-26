package fr.uge.jee.springmvc.pokematch.controllers;

import fr.uge.jee.springmvc.pokematch.models.PokeManager;
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
    private PokeManager pokeManager;

    @ModelAttribute("user")
    public User user(){
        return new User();
    }

    @Autowired
    public void setPokeManager(PokeManager pokemanager) {
        this.pokeManager = Objects.requireNonNull(pokemanager);
    }

    @GetMapping("/pokematch")
    public String greeting(Model model, @ModelAttribute("hi") User user) {
        model.addAttribute("user", user);
        pokeManager.getPokemons().forEach(System.out::println);
        return "pokematch";
    }

    @PostMapping("/pokematch")
    public String processForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "pokematch";
        }

        System.out.println(user.getFirstName() + user.getLastName());
        return "pokematch";
    }
}
