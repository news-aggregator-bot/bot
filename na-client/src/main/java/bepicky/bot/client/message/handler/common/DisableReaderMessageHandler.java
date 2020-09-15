package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static bepicky.bot.client.message.template.TemplateUtils.DISABLE_READER;

@Component
@Slf4j
public class DisableReaderMessageHandler implements CommonMessageHandler {

    public static final String STOP = "/stop";

    @Autowired
    private IReaderService readerService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public String trigger() {
        return STOP;
    }

    @Override
    public BotApiMethod<Message> handle(Message message) {
        log.info("reader:{}:disable:start", message.getChatId());
        ReaderDto disabled = readerService.disable(message.getChatId());
        log.info("reader:{}:disable:success", message.getChatId());
        String currentText = templateContext.processTemplate(DISABLE_READER, disabled.getLang());
        return new SendMessage().setText(currentText).setChatId(message.getChatId());
    }
}
