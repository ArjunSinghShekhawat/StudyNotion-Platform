package in.study_notion.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class HealthCheaker {

    @GetMapping("/ok")
    public String ok(){
        return "ok";
    }
}
