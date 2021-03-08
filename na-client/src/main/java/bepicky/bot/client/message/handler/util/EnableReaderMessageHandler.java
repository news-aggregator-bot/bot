package bepicky.bot.client.message.handler.util;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EnableReaderMessageHandler implements UtilMessageHandler {

    @Autowired
    private IReaderService readerService;

    @Autowired
    private ChatChainManager chainManager;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(ChatCommand cc) {
        ChatChainLink current = chainManager.current(cc.getChatId());
        log.info("reader:{}:enable:start", cc.getChatId());
        ReaderDto enabled = readerService.enable(cc.getChatId());
        log.info("reader:{}:enable:success", cc.getChatId());
        String currentText = templateContext.processTemplate(current.getMsgKey(), enabled.getLang());
        chainManager.clean(cc.getChatId());
        return new HandleResult(currentText, new InlineMarkupBuilder().build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.ENABLE_READER;
    }

}
