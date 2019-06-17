package com.mjbmjb.cf.codefellowship;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodefellowshipController {

    @GetMapping("/dinosaurs")
    public String getDinosaurs() {
        return "dinosaurs";
    }
}
