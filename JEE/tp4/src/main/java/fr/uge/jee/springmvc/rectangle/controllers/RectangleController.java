package fr.uge.jee.springmvc.rectangle.controllers;

import fr.uge.jee.springmvc.rectangle.models.Rectangle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RectangleController {

    @GetMapping("/rectangle")
    public String rectangleForm(Rectangle rectangle) {
        return "rectForm";
    }

    @PostMapping("/rectangle")
    public String processForm(@Valid @ModelAttribute("rectangle") Rectangle rectangle, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "rectForm";
        }

        model.addAttribute("area", rectangle.area());
        return "rectangle-result";
    }
}
