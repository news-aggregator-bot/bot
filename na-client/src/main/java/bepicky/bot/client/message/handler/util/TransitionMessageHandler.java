package bepicky.bot.client.message.handler.util;

import bepicky.bot.client.message.button.CommandType;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowManager;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

@Component
public class TransitionMessageHandler implements UtilMessageHandler {

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private ChatFlowManager flowContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public HandleResult handle(Message message, String data) {
        ReaderDto readerDto = readerService.find(message.getChatId());
        MarkupBuilder markup = new MarkupBuilder();
        ChatFlow next = flowContext.goNext(message.getChatId());
        String nextText = templateContext.processTemplate(next.getMsgKey(), readerDto.getLang());
        String buttonText = templateContext.processEmojiTemplate(next.getButtonKey(), readerDto.getLang());
        MarkupBuilder.Button button = markup.button(buttonText, next.getCommand());
        markup.addButtons(Arrays.asList(button));

        return new HandleResult(nextText, markup.build());
    }

    @Override
    public String trigger() {
        return CommandType.TRANSITION.name();
    }
}
