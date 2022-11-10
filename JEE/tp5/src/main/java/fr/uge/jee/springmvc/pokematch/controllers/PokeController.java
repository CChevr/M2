package fr.uge.jee.springmvc.pokematch.controllers;

import fr.uge.jee.springmvc.pokematch.history.IHistory;
import fr.uge.jee.springmvc.pokematch.models.Pokedex;
import fr.uge.jee.springmvc.pokematch.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@SessionAttributes("user")
public class PokeController {
    private Pokedex pokedex;
    private IHistory history;
    @Value("${pokematch.historysize}")
    private int historySize;

    @ModelAttribute("user")
    public User user(){
        return new User();
    }

    @Autowired
    public void setPokedex(Pokedex pokedex) {
        this.pokedex = Objects.requireNonNull(pokedex);
    }

    @Autowired
    public void setHistory(IHistory history) { this.history = Objects.requireNonNull(history); }

    @GetMapping("/pokematch")
    public String greeting(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("history", history.getLastPokemons(historySize));
        return "pokematch";
    }

    @PostMapping("/pokematch")
    public String processForm(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("history", history.getLastPokemons(historySize));
            return "pokematch";
        }

        var id = user.getFirstName().hashCode() + user.getLastName().hashCode();
        var pokemon = pokedex.getPokemon(id);
        history.addComputation(pokemon);

        model.addAttribute("history", history.getLastPokemons(historySize));
        model.addAttribute("pokemon", pokemon);

        return "pokematch";
    }
}
