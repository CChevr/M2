package fr.uge.jee.servlet.rectanglesession.controllers;

import fr.uge.jee.servlet.rectanglesession.models.Rectangle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("session")
public class RectangleController {
    private final List<Integer> areas = new ArrayList<>();

    @GetMapping("/rectangleSession")
    public String rectangleForm(Rectangle rectangle, Model model) {
        model.addAttribute("areas", areas);
        return "rectFormSession";
    }

    @PostMapping("/rectangleSession")
    public String processForm(@Valid @ModelAttribute("rectangle") Rectangle rectangle, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "rectFormSession";
        }

        areas.add(rectangle.area());
        model.addAttribute("area", rectangle.area());

        return "rectangle-result";
    }
}
