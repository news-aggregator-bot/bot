package bepicky.bot.client.message.handler.common;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class OptionsCommonMessageHandler implements MessageHandler {

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private CommandManager cmdMngr;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        ReaderDto reader = readerService.find(message.getChatId());

        InlineMarkupBuilder builder = new InlineMarkupBuilder();
        String settingsText = templateContext.processTemplate(ButtonNames.SETTINGS, reader.getLang());

        Tuple2<String, CommandType> activationKeys = reader.isEnabled() ?
            Tuples.of(ButtonNames.DISABLE_READER, CommandType.PAUSE_READER)
            : Tuples.of(ButtonNames.ENABLE_READER, CommandType.ENABLE_READER);

        InlineMarkupBuilder.InlineButton activationButton = builder.button(
            templateContext.processEmojiTemplate(activationKeys.getT1(), reader.getLang()),
            activationKeys.getT2().getKey()
        );
        builder.addButton(builder.button(settingsText, cmdMngr.status(), CommandType.SETTINGS.getKey()))
            .addButton(activationButton);

        return new SendMessage()
            .setText(templateContext.processTemplate(TemplateNames.OPTIONS, reader.getLang()))
            .setChatId(message.getChatId()).setReplyMarkup(builder.build());
    }

    @Override
    public String trigger() {
        return "/options";
    }

}
