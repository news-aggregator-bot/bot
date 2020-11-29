package bepicky.bot.client.controller;

import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.router.PickyNewsBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UtilController {

    @Autowired
    private PickyNewsBot bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }

}
