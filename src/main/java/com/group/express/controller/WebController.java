package com.group.express.controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController implements ErrorController {

    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String index() {
        return "forward:/index.html"; // index.html 경로로 수정
    }

}
