package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.command.CommandType;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.template.TemplateUtils.PAUSE_READER;

@Component
@Slf4j
public class PauseReaderMessageHandler implements UtilMessageHandler {

    @Autowired
    private IReaderService readerService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(ChatCommand cc) {
        log.debug("reader:{}:pause:start", cc.getChatId());
        ReaderDto disabled = readerService.pause(cc.getChatId());
        log.debug("reader:{}:pause:success", cc.getChatId());
        String currentText = templateContext.processTemplate(PAUSE_READER, disabled.getLang());
        return new HandleResult(currentText, new InlineMarkupBuilder().build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.PAUSE_READER;
    }

}
