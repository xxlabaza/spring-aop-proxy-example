package ru.xxlabaza.aspect;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Artem Labazin
 * @version 1.0.0
 * @since Dec 30, 2015 | 12:39
 */
@RestController
class MyController {

    @RequestMapping(params = "text")
    public String callMeMaybe (@RequestParam("text") String text) {
        if (text.equalsIgnoreCase("exception")) {
            throw new RuntimeException();
        }
        return "ok";
    }
}
