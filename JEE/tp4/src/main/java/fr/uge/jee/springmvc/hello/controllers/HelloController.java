package fr.uge.jee.springmvc.hello.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String greeting(Model model) {
        model.addAttribute("name", "Arnaud");
        return "hello";
    }
}
