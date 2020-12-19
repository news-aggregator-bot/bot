package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.command.ChatCommand;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public abstract class NavigationMessageHandler implements UtilMessageHandler {

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    protected ChatChainManager chainManager;

    @Autowired
    private IReaderService readerService;

    @Override
    public HandleResult handle(ChatCommand cc) {
        ReaderDto readerDto = readerService.find(cc.getChatId());
        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        ChatChainLink direction = getDirection(cc.getChatId());
        String nextText = templateContext.processTemplate(direction.getMsgKey(), readerDto.getLang());
        String buttonText = templateContext.processEmojiTemplate(direction.getButtonKey(), readerDto.getLang());
        InlineMarkupBuilder.InlineButton button = markup.button(buttonText, direction.getCommand());
        markup.addButtons(Arrays.asList(button));

        return new HandleResult(nextText, markup.build());
    }

    protected abstract ChatChainLink getDirection(long chatId);

}
