package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.handler.context.ChatFlow;
import bepicky.bot.client.message.handler.context.ChatFlowContext;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class TransitionMessageHandler implements CallbackMessageHandler {

    public static final String TRANSITION = "transition";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private ChatFlowContext flowContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public HandleResult handle(Message message, String data) {
        ReaderDto readerDto = readerService.find(message.getChatId());
        MarkupBuilder markup = new MarkupBuilder();
        ChatFlow next = flowContext.goNext(message.getChatId());
        String nextText = templateContext.processTemplate(next.getMsgKey(), readerDto.getLang());
        String buttonText = parseToUnicode(templateContext.processTemplate(next.getButtonKey(), readerDto.getLang()));
        MarkupBuilder.Button button = markup.button(buttonText, next.getCommand());
        markup.addButtons(Arrays.asList(button));

        return new HandleResult(nextText, markup.build());
    }

    @Override
    public String trigger() {
        return TRANSITION;
    }
}
