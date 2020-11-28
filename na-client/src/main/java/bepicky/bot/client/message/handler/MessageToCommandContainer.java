package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.LangUtils;
import bepicky.bot.client.message.handler.common.HelpMessageHandler;
import bepicky.bot.client.message.template.MessageTemplateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageToCommandContainer {

    private final Map<String, String> message2Command = new HashMap<>();

    @Autowired
    private MessageTemplateContext templateContext;

    @PostConstruct
    public void initContainer() {
        LangUtils.SUPPORTED.forEach(l -> message2Command.put(templateContext.helpButton(l), HelpMessageHandler.HELP));
    }

    public String getCommand(String message) {
        return message2Command.get(message);
    }

}
