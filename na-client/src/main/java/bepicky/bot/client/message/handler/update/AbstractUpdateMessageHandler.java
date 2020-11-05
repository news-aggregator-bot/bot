package bepicky.bot.client.message.handler.update;

import bepicky.bot.client.message.button.CommandBuilder;
import bepicky.bot.client.message.button.MarkupBuilder;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateUtils;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import static bepicky.bot.client.message.button.CommandType.LIST_NOT_PICKED;
import static bepicky.bot.client.message.button.CommandType.LIST_PICKED;
import static bepicky.bot.client.message.button.CommandType.SETTINGS;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_ADD;
import static bepicky.bot.client.message.template.ButtonNames.SETTINGS_REMOVE;

public abstract class AbstractUpdateMessageHandler implements UpdateMessageHandler {

    @Autowired
    protected CommandBuilder commandBuilder;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected IReaderService readerService;

    @Override
    public HandleResult handle(Message message, String data) {
        ReaderDto reader = readerService.disable(message.getChatId());

        MarkupBuilder markup = new MarkupBuilder();
        String lang = reader.getLang();

        MarkupBuilder.Button addButton = markup.button(
            templateContext.processEmojiTemplate(SETTINGS_ADD, lang),
            commandBuilder.list(LIST_NOT_PICKED, trigger())
        );
        MarkupBuilder.Button removeButton = markup.button(
            templateContext.processEmojiTemplate(SETTINGS_REMOVE, lang),
            commandBuilder.list(LIST_PICKED, trigger())
        );
        MarkupBuilder.Button backButton = markup.button(
            templateContext.processEmojiTemplate(ButtonNames.DIR_BACK, lang),
            SETTINGS.name()
        );
        markup.addButton(addButton)
            .addButton(removeButton)
            .addButton(backButton);
        String text = templateContext.processTemplate(
            TemplateUtils.SETTINGS_UPDATE,
            lang,
            TemplateUtils.name(trigger())
        );
        return new HandleResult(text, markup.build());
    }
}
