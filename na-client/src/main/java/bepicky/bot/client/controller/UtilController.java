package bepicky.bot.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UtilController {

    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }

}
