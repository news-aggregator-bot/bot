package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowContext;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class EnableReaderMessageHandler implements UtilMessageHandler {

    @Autowired
    private IReaderService readerService;

    @Autowired
    private ChatFlowContext flowContext;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(Message message, String data) {
        ChatFlow current = flowContext.current(message.getChatId());
        log.info("reader:{}:enable:start", message.getChatId());
        ReaderDto enabled = readerService.enable(message.getChatId());
        log.info("reader:{}:enable:success", message.getChatId());
        String currentText = templateContext.processTemplate(current.getMsgKey(), enabled.getLang());
        flowContext.clean(message.getChatId());
        return new HandleResult(currentText, new MarkupBuilder().build());
    }

    @Override
    public String trigger() {
        return CommandBuilder.ENABLE_READER;
    }
}
