package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.handler.common.HelpMessageHandler;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.cmd.CommandTranslator;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.template.MessageTemplateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageToCommandTranslator implements CommandTranslator {

    private final Map<String, String> message2Command = new HashMap<>();

    @Autowired
    private MessageTemplateContext templateContext;

    @PostConstruct
    public void initContainer() {
        LangUtils.SUPPORTED.forEach(l -> message2Command.put(
            templateContext.processTemplate(ButtonNames.HELP, l), HelpMessageHandler.HELP)
        );
    }

    @Override
    public String translate(String text) {
        return message2Command.get(text);
    }
}
